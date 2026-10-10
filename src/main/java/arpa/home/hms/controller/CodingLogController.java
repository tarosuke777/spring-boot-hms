package arpa.home.hms.controller;

import arpa.home.hms.exception.IllegalRequestException;
import arpa.home.hms.form.CodingLogForm;
import arpa.home.hms.security.LoginUser;
import arpa.home.hms.service.CodingLogService;
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
@RequestMapping("/codingLog")
@RequiredArgsConstructor
public class CodingLogController {

  private static final String REDIRECT_LIST = "redirect:/codingLog/list";
  private static final String LIST_VIEW = "codingLog/list";
  private static final String DETAIL_VIEW = "codingLog/detail";
  private static final String REGISTER_VIEW = "codingLog/register";

  private final CodingLogService codingLogService;

  @GetMapping("/list")
  public String getList(@PageableDefault(size = 10) Pageable pageable, Model model,
      @AuthenticationPrincipal LoginUser user) {
    model.addAttribute("codingLogPage", codingLogService.getCodingLogList(user.getId(), pageable));
    return LIST_VIEW;
  }

  @GetMapping("/detail/{id}")
  public String getDetail(@PathVariable Integer id, Model model,
      @AuthenticationPrincipal LoginUser user) {
    model.addAttribute("codingLogForm", codingLogService.getCodingLogDetails(id, user.getId()));
    return DETAIL_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute CodingLogForm codingLogForm) {
    if (codingLogForm.getSolvedDate() == null) {
      codingLogForm.setSolvedDate(LocalDate.now(ZoneId.systemDefault()));
    }
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated CodingLogForm form, BindingResult bindingResult,
      @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasErrors()) {
      return REGISTER_VIEW;
    }

    codingLogService.registerCodingLog(form, user.getId());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) CodingLogForm form,
      BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasFieldErrors(CodingLogForm.Fields.id)
        || bindingResult.hasFieldErrors(CodingLogForm.Fields.version)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }
    if (bindingResult.hasErrors()) {
      return DETAIL_VIEW;
    }

    codingLogService.updateCodingLog(form, user.getId());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) CodingLogForm form,
      BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasFieldErrors(CodingLogForm.Fields.id)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    codingLogService.deleteCodingLog(Objects.requireNonNull(form.getId()), user.getId());
    return REDIRECT_LIST;
  }
}
