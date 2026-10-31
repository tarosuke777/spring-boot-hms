package arpa.home.hms.controller;

import arpa.home.hms.enums.ActivityLogTargetType;
import arpa.home.hms.exception.IllegalRequestException;
import arpa.home.hms.form.ActivityLogForm;
import arpa.home.hms.security.LoginUser;
import arpa.home.hms.service.ActivityLogService;
import arpa.home.hms.service.BookService;
import arpa.home.hms.service.GameVideoService;
import arpa.home.hms.service.MovieService;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activityLog")
@RequiredArgsConstructor
public class ActivityLogController {

  private static final String REDIRECT_LIST = "redirect:/activityLog/list";
  private static final String LIST_VIEW = "activityLog/list";
  private static final String DETAIL_VIEW = "activityLog/detail";
  private static final String REGISTER_VIEW = "activityLog/register";

  private final ActivityLogService activityLogService;
  private final BookService bookService;
  private final MovieService movieService;
  private final GameVideoService gameVideoService;

  @ModelAttribute("activityLogTargetTypes")
  public ActivityLogTargetType[] getActivityLogTargetTypes() {
    return ActivityLogTargetType.values();
  }

  @GetMapping("/list")
  public String getList(@PageableDefault(size = 10) Pageable pageable, Model model,
      @AuthenticationPrincipal LoginUser user) {
    model.addAttribute("activityLogPage",
        activityLogService.getActivityLogList(user.getId(), pageable));
    model.addAttribute("bookMap", bookService.getBookMap(user.getId()));
    model.addAttribute("movieMap", movieService.getMovieMap(user.getId()));
    model.addAttribute("gameVideoMap", gameVideoService.getGameVideoMap());
    return LIST_VIEW;
  }

  @GetMapping("/detail/{id}")
  public String getDetail(@PathVariable Integer id, Model model,
      @AuthenticationPrincipal LoginUser user) {
    model.addAttribute("activityLogForm",
        activityLogService.getActivityLogDetails(id, user.getId()));
    addTargetOptions(model, user);
    return DETAIL_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute ActivityLogForm activityLogForm, Model model,
      @AuthenticationPrincipal LoginUser user) {
    if (activityLogForm.getActivityDate() == null) {
      activityLogForm.setActivityDate(LocalDate.now(ZoneId.systemDefault()));
    }
    addTargetOptions(model, user);
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated ActivityLogForm form,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasErrors()) {
      addTargetOptions(model, user);
      return REGISTER_VIEW;
    }

    if (!activityLogService.registerActivityLog(form, user.getId())) {
      bindingResult.rejectValue("targetId", "activityLog.targetId.invalid", "選択されたタイトルが見つかりません。");
      addTargetOptions(model, user);
      return REGISTER_VIEW;
    }
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) ActivityLogForm form,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasFieldErrors(ActivityLogForm.Fields.id)
        || bindingResult.hasFieldErrors(ActivityLogForm.Fields.version)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }
    if (bindingResult.hasErrors()) {
      addTargetOptions(model, user);
      return DETAIL_VIEW;
    }

    if (!activityLogService.updateActivityLog(form, user.getId())) {
      bindingResult.rejectValue("targetId", "activityLog.targetId.invalid", "選択されたタイトルが見つかりません。");
      addTargetOptions(model, user);
      return DETAIL_VIEW;
    }
    return REDIRECT_LIST;
  }

  private void addTargetOptions(Model model, LoginUser user) {
    model.addAttribute("bookMap", bookService.getAdultBookMap(user.getId()));
    model.addAttribute("movieMap", movieService.getAdultMovieMap(user.getId()));
    model.addAttribute("gameVideoMap", gameVideoService.getGameVideoMap());
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) ActivityLogForm form,
      BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasFieldErrors(ActivityLogForm.Fields.id)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    activityLogService.deleteActivityLog(Objects.requireNonNull(form.getId()), user.getId());
    return REDIRECT_LIST;
  }
}
