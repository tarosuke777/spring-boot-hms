package arpa.home.hms.controller;

import arpa.home.hms.enums.TargetArea;
import arpa.home.hms.form.TrainingMenuForm;
import arpa.home.hms.security.LoginUser;
import arpa.home.hms.service.TrainingMenuService;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/trainingMenu")
@RequiredArgsConstructor
public class TrainingMenuController {

  private static final String REDIRECT_LIST = "redirect:/trainingMenu/list";
  private static final String LIST_VIEW = "trainingMenu/list";
  private static final String DETAIL_VIEW = "trainingMenu/detail";
  private static final String REGISTER_VIEW = "trainingMenu/register";

  private final TrainingMenuService trainingMenuService;

  @GetMapping("/list")
  public String getList(Model model, @AuthenticationPrincipal LoginUser user) {
    model.addAttribute("trainingMenuList", trainingMenuService.getTrainingMenuList(user.getId()));
    model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
    return LIST_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute TrainingMenuForm form, Model model) {
    model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated TrainingMenuForm form,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }
    trainingMenuService.registerTrainingMenu(form);
    return REDIRECT_LIST;
  }

  @GetMapping("/detail/{id}")
  public String getDetail(@PathVariable("id") Integer id, Model model,
      @AuthenticationPrincipal LoginUser user) {
    TrainingMenuForm form = trainingMenuService.getTrainingMenuDetails(id, user.getId());
    model.addAttribute("trainingMenuForm", form);
    model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
    return DETAIL_VIEW;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) TrainingMenuForm form,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
      return DETAIL_VIEW;
    }
    trainingMenuService.updateTrainingMenu(form, user.getId());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@ModelAttribute @Validated(DeleteGroup.class) TrainingMenuForm form,
      Model model, @AuthenticationPrincipal LoginUser user) {
    trainingMenuService.deleteTrainingMenu(Objects.requireNonNull(form.getId()), user.getId());
    return REDIRECT_LIST;
  }
}
