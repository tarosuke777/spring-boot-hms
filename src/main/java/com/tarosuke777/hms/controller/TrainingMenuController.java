package com.tarosuke777.hms.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tarosuke777.hms.domain.TrainingMenuService;
import com.tarosuke777.hms.enums.TargetArea;
import com.tarosuke777.hms.form.TrainingMenuForm;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public String getList(Model model, @AuthenticationPrincipal UserDetails UserDetail) {
		model.addAttribute("trainingMenuList",
				trainingMenuService.getTrainingMenuList(UserDetail.getUsername()));
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

	@GetMapping("/detail/{trainingMenuId}")
	public String getDetail(@PathVariable("trainingMenuId") Integer trainingMenuId, Model model,
			@AuthenticationPrincipal UserDetails user) {
		TrainingMenuForm form =
				trainingMenuService.getTrainingMenuDetails(trainingMenuId, user.getUsername());
		model.addAttribute("trainingMenuForm", form);
		model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
		return DETAIL_VIEW;
	}

	@PostMapping(value = "detail", params = "update")
	public String update(@ModelAttribute @Validated(UpdateGroup.class) TrainingMenuForm form,
			BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails user) {
		if (bindingResult.hasErrors()) {
			return DETAIL_VIEW;
		}
		trainingMenuService.updateTrainingMenu(form, user.getUsername());
		return REDIRECT_LIST;
	}

	@PostMapping(value = "/detail", params = "delete")
	public String delete(@ModelAttribute @Validated(DeleteGroup.class) TrainingMenuForm form,
			Model model, @AuthenticationPrincipal UserDetails user) {
		trainingMenuService.deleteTrainingMenu(form.getTrainingMenuId(), user.getUsername());
		return REDIRECT_LIST;
	}
}
