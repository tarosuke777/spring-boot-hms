package arpa.home.hms.service;

import arpa.home.hms.entity.BookReadingLogEntity;
import arpa.home.hms.form.BookReadingLogForm;
import arpa.home.hms.mapper.BookReadingLogMapper;
import arpa.home.hms.repository.BookReadingLogRepository;
import arpa.home.hms.repository.BookRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookReadingLogService {

  private final BookReadingLogRepository bookReadingLogRepository;
  private final BookRepository bookRepository;
  private final BookReadingLogMapper bookReadingLogMapper;

  public Page<BookReadingLogForm> getBookReadingLogList(Integer currentUserId,
      @NonNull Pageable pageable) {
    return bookReadingLogRepository.findByCreatedByOrderByReadDateDesc(currentUserId, pageable)
        .map(log -> {
          BookReadingLogForm form = bookReadingLogMapper.toForm(log);
          form.setBookId(log.getBook().getId());
          return form;
        });
  }

  public List<BookReadingLogForm> getLatestBookReadingLogs(Integer currentUserId, int limit) {
    return bookReadingLogRepository
        .findByCreatedByOrderByReadDateDescIdDesc(currentUserId, PageRequest.of(0, limit)).stream()
        .map(log -> {
          BookReadingLogForm form = bookReadingLogMapper.toForm(log);
          form.setBookId(log.getBook().getId());
          return form;
        }).toList();
  }

  public BookReadingLogForm getBookReadingLogDetails(Integer id, Integer currentUserId) {
    BookReadingLogEntity log = bookReadingLogRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Book reading log not found or access denied"));

    BookReadingLogForm form = bookReadingLogMapper.toForm(log);
    form.setBookId(log.getBook().getId());
    return form;
  }

  @Transactional
  public void registerBookReadingLog(BookReadingLogForm form, Integer currentUserId) {
    BookReadingLogEntity entity = Objects.requireNonNull(bookReadingLogMapper.toEntity(form));
    entity.setBook(bookRepository.findByIdAndCreatedBy(form.getBookId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Book not found or access denied")));
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
      entity.setBook(bookRepository.findByIdAndCreatedBy(form.getBookId(), currentUserId)
          .orElseThrow(() -> new RuntimeException("Book not found or access denied")));
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
