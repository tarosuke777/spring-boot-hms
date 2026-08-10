package com.tarosuke777.hms.service;

import com.tarosuke777.hms.entity.GoogleCalendarEntity;
import com.tarosuke777.hms.form.GoogleCalendarForm;
import com.tarosuke777.hms.mapper.GoogleCalendarMapper;
import com.tarosuke777.hms.repository.GoogleCalendarRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoogleCalendarService {

  private final GoogleCalendarRepository googleCalendarRepository;
  private final GoogleCalendarMapper googleCalendarMapper;

  /**
   * ログインユーザーのGoogleカレンダー一覧を取得します。
   */
  public List<GoogleCalendarForm> getCalendarList(Integer currentUserId) {
    return googleCalendarRepository.findByCreatedBy(currentUserId).stream()
        .map(googleCalendarMapper::toForm).toList();
  }

  /**
   * 単一のカレンダー情報を取得します。
   */
  public GoogleCalendarForm getCalendar(Integer id, Integer currentUserId) {
    GoogleCalendarEntity entity = googleCalendarRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Google Calendar not found or unauthorized"));
    return googleCalendarMapper.toForm(entity);
  }

  /**
   * ダッシュボード埋め込み用のGoogleカレンダーURLを生成します。 複数のカレンダーIDとカラー設定を1つのURLに結合します。
   */
  public String getEmbedUrl(Integer currentUserId) {
    List<GoogleCalendarEntity> calendars = googleCalendarRepository.findByCreatedBy(currentUserId);

    if (calendars.isEmpty()) {
      return "";
    }

    StringBuilder url = new StringBuilder(
        "https://calendar.google.com/calendar/embed?ctz=Asia/Tokyo&mode=AGENDA&showTitle=0&showPrint=0&showTabs=0&showTz=0&showCalendars=0");

    for (GoogleCalendarEntity cal : calendars) {
      if (cal.getCalendarId() != null && !cal.getCalendarId().isBlank()) {
        url.append("&src=").append(cal.getCalendarId());

        if (cal.getColor() != null && !cal.getColor().isBlank()) {
          // '#' が含まれている場合は除去して %23（URLエンコード）を付与
          String cleanColor = cal.getColor().replace("#", "");
          url.append("&color=%23").append(cleanColor);
        }
      }
    }

    return url.toString();
  }

  /**
   * カレンダー情報を新規登録します。
   */
  @Transactional
  public void createCalendar(GoogleCalendarForm form) {
    GoogleCalendarEntity entity = Objects.requireNonNull(googleCalendarMapper.toEntity(form));
    googleCalendarRepository.save(entity);
  }

  /**
   * カレンダー情報を更新します。
   */
  @Transactional
  public void updateCalendar(GoogleCalendarForm form, Integer currentUserId) {
    GoogleCalendarEntity existEntity =
        googleCalendarRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Google Calendar not found or unauthorized"));

    GoogleCalendarEntity entity = Objects.requireNonNull(googleCalendarMapper.copy(existEntity));
    googleCalendarMapper.updateEntityFromForm(form, entity);
    googleCalendarRepository.save(entity);
  }

  /**
   * カレンダー情報を削除します。
   */
  @Transactional
  public void deleteCalendar(@NonNull Integer id, Integer currentUserId) {
    if (!googleCalendarRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Google Calendar not found or unauthorized");
    }

    googleCalendarRepository.deleteById(id);
  }
}
