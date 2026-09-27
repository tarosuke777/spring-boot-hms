package arpa.home.hms.controller;

import arpa.home.hms.enums.BusinessGenre;
import arpa.home.hms.enums.BusinessStatus;
import arpa.home.hms.exception.IllegalRequestException;
import arpa.home.hms.form.BusinessForm;
import arpa.home.hms.security.LoginUser;
import arpa.home.hms.service.BusinessService;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

  private static final String REDIRECT_LIST = "redirect:/business/list";
  private static final String LIST_VIEW = "business/list";
  private static final String DETAIL_VIEW = "business/detail";
  private static final String REGISTER_VIEW = "business/register";
  private static final String REDIRECT_DETAIL_VIEW = "redirect:/business/detail/{id}";

  private final BusinessService businessService;

  @GetMapping("/list")
  public String getList(@RequestParam(required = false) String keyword,
      @RequestParam(required = false) BusinessGenre genre,
      @RequestParam(required = false) BusinessStatus status,
      @PageableDefault(size = 10) Pageable pageable, Model model,
      @AuthenticationPrincipal LoginUser user) {

    var businessPage = businessService.getBusinessList(user.getId(), keyword, genre, status,
        Objects.requireNonNull(pageable));
    model.addAttribute("businessPage", businessPage);
    model.addAttribute("keyword", keyword);
    model.addAttribute("genre", genre);
    model.addAttribute("status", status);
    return LIST_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute BusinessForm form) {
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated BusinessForm form, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return REGISTER_VIEW;
    }
    businessService.registerBusiness(form);
    return REDIRECT_LIST;
  }

  @GetMapping("/detail/{id}")
  public String getDetail(@PathVariable("id") Integer id, Model model,
      @AuthenticationPrincipal LoginUser user) {
    BusinessForm form = businessService.getBusiness(id, user.getId());
    model.addAttribute("businessForm", form);
    return DETAIL_VIEW;
  }

  @PostMapping(value = "/detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) BusinessForm form,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {

    if (bindingResult.hasFieldErrors(BusinessForm.Fields.id)
        || bindingResult.hasFieldErrors(BusinessForm.Fields.version)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    if (bindingResult.hasErrors()) {
      model.addAttribute("businessForm", form);
      return DETAIL_VIEW;
    }

    businessService.updateBusiness(form, user.getId());

    Map<String, Object> uriVariables = new HashMap<>();
    uriVariables.put(BusinessForm.Fields.id, form.getId());
    return UriComponentsBuilder.fromUriString(REDIRECT_DETAIL_VIEW).buildAndExpand(uriVariables)
        .toUriString();
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) BusinessForm form, BindingResult bindingResult,
      @AuthenticationPrincipal LoginUser user) {

    if (bindingResult.hasFieldErrors(BusinessForm.Fields.id)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    businessService.deleteBusiness(Objects.requireNonNull(form.getId()), user.getId());
    return REDIRECT_LIST;
  }
}
