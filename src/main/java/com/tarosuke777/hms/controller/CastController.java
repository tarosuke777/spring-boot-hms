package com.tarosuke777.hms.controller;

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
import com.tarosuke777.hms.exception.IllegalRequestException;
import com.tarosuke777.hms.form.CastForm;
import com.tarosuke777.hms.security.LoginUser;
import com.tarosuke777.hms.service.CastService;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/cast")
@RequiredArgsConstructor
public class CastController {

    private static final String REDIRECT_LIST = "redirect:/cast/list";
    private static final String LIST_VIEW = "cast/list";
    private static final String DETAIL_VIEW = "cast/detail";
    private static final String REGISTER_VIEW = "cast/register";

    private final CastService castService;

    @GetMapping("/list")
    public String getList(Model model, @AuthenticationPrincipal LoginUser user) {
        model.addAttribute("castList", castService.getCastList(user.getId()));
        return LIST_VIEW;
    }

    @GetMapping("/register")
    public String getRegister(@ModelAttribute CastForm form) {
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Validated CastForm form, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }
        castService.registerCast(form);
        return REDIRECT_LIST;
    }

    @GetMapping("/detail/{castId}")
    public String getDetail(@PathVariable("castId") Integer castId, Model model,
            @AuthenticationPrincipal LoginUser user) {
        CastForm form = castService.getCast(castId, user.getId());
        model.addAttribute("castForm", form);
        return DETAIL_VIEW;
    }

    @PostMapping(value = "detail", params = "update")
    public String update(@ModelAttribute @Validated(UpdateGroup.class) CastForm form,
            BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {

        // id や version にエラーがある場合は、改ざんとみなしてシステムエラー
        if (bindingResult.hasFieldErrors(CastForm.Fields.id)
                || bindingResult.hasFieldErrors(CastForm.Fields.version)) {
            throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
        }

        if (bindingResult.hasErrors()) {
            return DETAIL_VIEW;
        }
        castService.updateCast(form, user.getId());
        return REDIRECT_LIST;
    }

    @PostMapping(value = "/detail", params = "delete")
    public String delete(@Validated(DeleteGroup.class) CastForm form, BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser user) {

        // id にエラーがある場合は改ざんとみなしてシステムエラー
        if (bindingResult.hasFieldErrors(CastForm.Fields.id)) {
            throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
        }

        castService.deleteCast(form.getId(), user.getId());
        return REDIRECT_LIST;
    }

}
