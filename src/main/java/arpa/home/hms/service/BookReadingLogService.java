package arpa.home.hms.service;

import arpa.home.hms.entity.BookEntity;
import arpa.home.hms.entity.BookReadingLogEntity;
import arpa.home.hms.form.BookReadingLogForm;
import arpa.home.hms.mapper.BookReadingLogMapper;
import arpa.home.hms.repository.BookReadingLogRepository;
import jakarta.persistence.EntityManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookReadingLogService {

  private final BookReadingLogRepository bookReadingLogRepository;
  private final BookReadingLogMapper bookReadingLogMapper;
  private final EntityManager entityManager;

  public Page<BookReadingLogForm> getBookReadingLogList(Integer currentUserId,
      @NonNull Pageable pageable) {
    return bookReadingLogRepository.findByCreatedByOrderByReadDateDesc(currentUserId, pageable)
        .map(log -> {
          BookReadingLogForm form = bookReadingLogMapper.toForm(log);
          form.setBookId(log.getBook().getId());
          return form;
        });
  }

  public BookReadingLogForm getBookReadingLogDetails(Integer id, Integer currentUserId) {
    BookReadingLogEntity log = bookReadingLogRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Book reading log not found or access denied"));

    BookReadingLogForm form = bookReadingLogMapper.toForm(log);
    form.setBookId(log.getBook().getId());
    return form;
  }

  @Transactional
  public void registerBookReadingLog(BookReadingLogForm form) {
    BookReadingLogEntity entity = Objects.requireNonNull(bookReadingLogMapper.toEntity(form));
    entity.setBook(entityManager.getReference(BookEntity.class, form.getBookId()));
    bookReadingLogRepository.save(entity);
  }

  @Transactional
  public void updateBookReadingLog(BookReadingLogForm form, Integer currentUserId) {
    BookReadingLogEntity existEntity =
        bookReadingLogRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Book reading log not found or access denied"));

    BookReadingLogEntity entity = Objects.requireNonNull(bookReadingLogMapper.copy(existEntity));
    bookReadingLogMapper.updateEntityFromForm(form, entity);
    if (form.getBookId() != null) {
      entity.setBook(entityManager.getReference(BookEntity.class, form.getBookId()));
    }
    bookReadingLogRepository.save(entity);
  }

  @Transactional
  public void deleteBookReadingLog(@NonNull Integer id, Integer currentUserId) {
    if (!bookReadingLogRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Book reading log not found or access denied");
    }
    bookReadingLogRepository.deleteById(id);
  }
}
