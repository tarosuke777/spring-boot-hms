package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.GoogleCalendarEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleCalendarRepository extends JpaRepository<GoogleCalendarEntity, Integer> {

  /**
   * 作成者（ログインユーザー）のIDに紐づくGoogleカレンダー一覧を取得します。
   *
   * @param createdBy 作成者ユーザーID
   * @return カレンダーエンティティのリスト
   */
  List<GoogleCalendarEntity> findByCreatedBy(Integer createdBy);

  /**
   * 指定したユーザーの特定のカレンダーが存在するか確認します（重複チェック用など）。
   *
   * @param calendarId GoogleカレンダーID
   * @param createdBy 作成者ユーザーID
   * @return 存在する場合は true
   */
  boolean existsByCalendarIdAndCreatedBy(String calendarId, Integer createdBy);

  /**
   * IDと作成者（ログインユーザー）のIDに紐づくGoogleカレンダーを取得します。
   *
   * @param id カレンダーID
   * @param createdBy 作成者ユーザーID
   * @return カレンダーエンティティ
   */
  Optional<GoogleCalendarEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  /**
   * 指定したユーザーの特定のカレンダーが存在するか確認します（重複チェック用など）。
   *
   * @param id カレンダーID
   * @param createdBy 作成者ユーザーID
   * @return 存在する場合は true
   */
  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
