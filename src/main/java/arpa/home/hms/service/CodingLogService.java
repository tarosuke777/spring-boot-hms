package arpa.home.hms.service;

import arpa.home.hms.entity.CodingLogEntity;
import arpa.home.hms.form.CodingLogForm;
import arpa.home.hms.mapper.CodingLogMapper;
import arpa.home.hms.repository.CodingLogRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CodingLogService {

  private final CodingLogRepository codingLogRepository;
  private final CodingLogMapper codingLogMapper;

  public Page<CodingLogForm> getCodingLogList(Integer currentUserId, @NonNull Pageable pageable) {
    return codingLogRepository.findByCreatedByOrderBySolvedDateDescIdDesc(currentUserId, pageable)
        .map(codingLogMapper::toForm);
  }

  public CodingLogForm getCodingLogDetails(Integer id, Integer currentUserId) {
    CodingLogEntity log = codingLogRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Coding log not found or access denied"));
    return codingLogMapper.toForm(log);
  }

  @Transactional
  public void registerCodingLog(CodingLogForm form, Integer currentUserId) {
    CodingLogEntity entity = Objects.requireNonNull(codingLogMapper.toEntity(form));
    entity.setCreatedBy(currentUserId);
    entity.setUpdatedBy(currentUserId);
    codingLogRepository.save(entity);
  }

  @Transactional
  public void updateCodingLog(CodingLogForm form, Integer currentUserId) {
    CodingLogEntity existingEntity =
        codingLogRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Coding log not found or access denied"));
    CodingLogEntity entity = Objects.requireNonNull(codingLogMapper.copy(existingEntity));
    codingLogMapper.updateEntityFromForm(form, entity);
    codingLogRepository.save(entity);
  }

  @Transactional
  public void deleteCodingLog(@NonNull Integer id, Integer currentUserId) {
    if (!codingLogRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Coding log not found or access denied");
    }
    codingLogRepository.deleteByIdAndCreatedBy(id, currentUserId);
  }
}
