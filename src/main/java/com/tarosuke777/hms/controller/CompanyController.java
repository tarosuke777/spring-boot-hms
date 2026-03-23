package com.tarosuke777.hms.controller;

import java.util.Map;
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
import org.springframework.web.util.UriComponentsBuilder;
import com.tarosuke777.hms.exception.IllegalRequestException;
import com.tarosuke777.hms.form.CompanyForm;
import com.tarosuke777.hms.security.LoginUser;
import com.tarosuke777.hms.service.CompanyService;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private static final String REDIRECT_LIST = "redirect:/company/list";
    private static final String LIST_VIEW = "company/list";
    private static final String DETAIL_VIEW = "company/detail";
    private static final String REDIRECT_DETAIL_VIEW = "redirect:/company/detail/{id}";
    private static final String REGISTER_VIEW = "company/register";

    private final CompanyService companyService;

    @GetMapping("/list")
    public String getList(Model model, @AuthenticationPrincipal LoginUser user) {
        model.addAttribute("companyList", companyService.getCompanyList(user.getId()));
        return LIST_VIEW;
    }

    @GetMapping("/register")
    public String getRegister(@ModelAttribute CompanyForm form) {
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Validated CompanyForm form, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }
        companyService.registerCompany(form);
        return REDIRECT_LIST;
    }

    @GetMapping("/detail/{companyId}")
    public String getDetail(@PathVariable("companyId") Integer companyId, Model model,
            @AuthenticationPrincipal LoginUser user) {
        CompanyForm form = companyService.getCompany(companyId, user.getId());
        model.addAttribute("companyForm", form);
        return DETAIL_VIEW;
    }

    @PostMapping(value = "detail", params = "update")
    public String update(@ModelAttribute @Validated(UpdateGroup.class) CompanyForm form,
            BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {

        // id や version にエラーがある場合は、改ざんとみなしてシステムエラー
        if (bindingResult.hasFieldErrors(CompanyForm.Fields.id)
                || bindingResult.hasFieldErrors(CompanyForm.Fields.version)) {
            throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
        }

        if (bindingResult.hasErrors()) {
            return DETAIL_VIEW;
        }
        companyService.updateCompany(form, user.getId());

        final Map<String, Object> uriVariables = Map.of("id", form.getId());
        return UriComponentsBuilder.fromUriString(REDIRECT_DETAIL_VIEW).buildAndExpand(uriVariables)
                .toUriString();
    }

    @PostMapping(value = "/detail", params = "delete")
    public String delete(@Validated(DeleteGroup.class) CompanyForm form,
            BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {

        // id にエラーがある場合は改ざんとみなしてシステムエラー
        if (bindingResult.hasFieldErrors(CompanyForm.Fields.id)) {
            throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
        }

        companyService.deleteCompany(form.getId(), user.getId());
        return REDIRECT_LIST;
    }

}
