package com.tarosuke777.hms.service;

import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.enums.BookGenre;
import com.tarosuke777.hms.form.BookForm;
import com.tarosuke777.hms.mapper.BookMapper;
import com.tarosuke777.hms.repository.BookRepository;
import com.tarosuke777.hms.specification.BookSpecifications;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final EntityManager entityManager;
  private final BookMapper bookMapper;

  private final VectorStore vectorStore;

  public Page<BookForm> getBookList(Integer currentUserId, BookGenre genre, Boolean isAdult,
      Pageable pageable) {
    var spec = BookSpecifications.withFilters(currentUserId, genre, isAdult);

    // Page<Entity> を取得
    Page<BookEntity> bookPage = bookRepository.findAll(spec, pageable);

    // Pageの中身(Entity)をFormに詰め替える
    return bookPage.map(book -> {
      BookForm form = bookMapper.toForm(book);
      if (book.getAuthor() != null) {
        form.setAuthorId(book.getAuthor().getId());
      }
      return form;
    });
  }

  public BookForm getBookDetails(Integer id, Integer currentUserId) {
    BookEntity book = bookRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Book not found"));
    BookForm bookForm = bookMapper.toForm(book);
    bookForm.setAuthorId(book.getAuthor().getId());
    return bookForm;
  }

  @Transactional
  public void registerBook(BookForm form) {
    BookEntity entity = bookMapper.toEntity(form);
    if (form.getAuthorId() != null) {
      entity.setAuthor(entityManager.getReference(AuthorEntity.class, form.getAuthorId()));
    }
    bookRepository.save(entity);

    // ベクトルストアに同期
    syncVectorStore(entity);
  }

  @Transactional
  public void updateBook(BookForm form, Integer currentUserId) {
    BookEntity existEntity = bookRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    BookEntity entity = bookMapper.copy(existEntity);
    if (existEntity.getAuthor() != null) {
      entity.setAuthor(
          entityManager.getReference(AuthorEntity.class, existEntity.getAuthor().getId()));
    }

    bookMapper.updateEntityFromForm(form, entity);
    if (form.getAuthorId() != null) {
      entity.setAuthor(entityManager.getReference(AuthorEntity.class, form.getAuthorId()));
    }
    bookRepository.save(entity);

    // ベクトルストアに同期
    syncVectorStore(entity);
  }

  @Transactional
  public void deleteBook(Integer id, Integer currentUserId) {
    if (!bookRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Book not found or access denied");
    }
    bookRepository.deleteById(id);

    vectorStore.delete(List.of(getVectorStoreId(id)));
  }

  /**
   * Entity の内容をベクトルストアに同期する共通メソッド
   */
  public void syncVectorStore(BookEntity entity) {
    // ベクトル検索の対象にしたいテキスト（タイトル + 内容など）を作成

    // 文章の組み立てにリンクを含める
    StringBuilder content = new StringBuilder();
    content
        .append(String.format("蔵書情報: タイトルは「%s」、ジャンルは「%s」です。", entity.getName(), entity.getGenre()));

    if (entity.getLink() != null && !entity.getLink().isBlank()) {
      content.append(String.format(" 詳細リンクはこちら: %s", entity.getLink()));
    }

    // 1. ID を "BOOK_" + 数値ID に固定する（これで他のIDと被らず、削除も容易になる）
    String docId = getVectorStoreId(entity.getId());

    // 2. メタデータにも "type" を入れておくと、検索時に「本だけ」に絞り込める
    Document doc =
        new Document(docId, content.toString(), Map.of("bookId", entity.getId(), "type", "BOOK"));

    try {
      vectorStore.add(List.of(doc));
    } catch (Exception e) {
      log.error("Failed to sync book to vector store: " + entity.getId(), e);
    }
  }

  @Transactional
  public void migrateAllBooksToVectorStore() {
    // 1. 全件取得（件数が多い場合は分割して取得するのが安全）
    List<BookEntity> allBooks = bookRepository.findAll();

    // 2. 1件ずつ（あるいはリストにまとめて）ベクトルストアへ登録
    for (BookEntity book : allBooks) {
      try {
        syncVectorStore(book);
      } catch (Exception e) {
        // 1件失敗しても止まらないようにログを出して継続
        log.error("Failed to migrate book ID: " + book.getId(), e);
      }
    }
  }

  private String getVectorStoreId(Integer bookId) {
    return "BOOK_" + bookId;
  }
}
