package com.tarosuke777.hms.controller;

import com.tarosuke777.hms.enums.TargetArea;
import com.tarosuke777.hms.form.DiaryForm;
import com.tarosuke777.hms.form.TrainingForm;
import com.tarosuke777.hms.security.LoginUser;
import com.tarosuke777.hms.service.DiaryService;
import com.tarosuke777.hms.service.GoogleCalendarService;
import com.tarosuke777.hms.service.TrainingMenuService;
import com.tarosuke777.hms.service.TrainingService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/top"})
@RequiredArgsConstructor
public class TopController {

  private final DiaryService diaryService;
  private final GoogleCalendarService googleCalendarService;
  private final TrainingService trainingService;
  private final TrainingMenuService trainingMenuService;

  /**
   * TOP（ダッシュボード）画面を表示
   */
  @GetMapping
  public String top(Model model, @AuthenticationPrincipal LoginUser user) {

    LocalDate today = LocalDate.now();
    DiaryForm todayDiary = diaryService.getDiaryByDate(today, user.getId()).orElse(null);

    model.addAttribute("today", today);
    model.addAttribute("todayDiary", todayDiary);

    String embedUrl = googleCalendarService.getEmbedUrl(user.getId());
    model.addAttribute("googleCalendarUrl", embedUrl);

    List<TrainingForm> latestTrainings = trainingService.getLatestTrainings(user.getId(), 5);
    model.addAttribute("latestTrainings", latestTrainings);
    model.addAttribute("trainingAreaMap", TargetArea.getTargetAreaMap());
    model.addAttribute("trainingMenuMap", trainingMenuService.getTrainingMenuMap());

    return "top";
  }
}
