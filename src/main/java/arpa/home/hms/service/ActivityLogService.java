package arpa.home.hms.service;

import arpa.home.hms.entity.ActivityLogEntity;
import arpa.home.hms.form.ActivityLogForm;
import arpa.home.hms.mapper.ActivityLogMapper;
import arpa.home.hms.repository.ActivityLogRepository;
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
public class ActivityLogService {

  private final ActivityLogRepository activityLogRepository;
  private final ActivityLogMapper activityLogMapper;

  public Page<ActivityLogForm> getActivityLogList(Integer currentUserId,
      @NonNull Pageable pageable) {
    return activityLogRepository
        .findByCreatedByOrderByActivityDateDescIdDesc(currentUserId, pageable)
        .map(activityLogMapper::toForm);
  }

  public List<ActivityLogForm> getLatestActivityLogs(Integer currentUserId, int limit) {
    return activityLogRepository
        .findByCreatedByOrderByActivityDateDescIdDesc(currentUserId, PageRequest.of(0, limit))
        .map(activityLogMapper::toForm).toList();
  }

  public ActivityLogForm getActivityLogDetails(Integer id, Integer currentUserId) {
    ActivityLogEntity log = activityLogRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Activity log not found or access denied"));
    return activityLogMapper.toForm(log);
  }

  @Transactional
  public void registerActivityLog(ActivityLogForm form, Integer currentUserId) {
    ActivityLogEntity entity = Objects.requireNonNull(activityLogMapper.toEntity(form));
    entity.setCreatedBy(currentUserId);
    entity.setUpdatedBy(currentUserId);
    activityLogRepository.save(entity);
  }

  @Transactional
  public void updateActivityLog(ActivityLogForm form, Integer currentUserId) {
    ActivityLogEntity existingEntity =
        activityLogRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Activity log not found or access denied"));
    ActivityLogEntity entity = Objects.requireNonNull(activityLogMapper.copy(existingEntity));
    activityLogMapper.updateEntityFromForm(form, entity);
    activityLogRepository.save(entity);
  }

  @Transactional
  public void deleteActivityLog(@NonNull Integer id, Integer currentUserId) {
    if (!activityLogRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Activity log not found or access denied");
    }
    activityLogRepository.deleteByIdAndCreatedBy(id, currentUserId);
  }
}
