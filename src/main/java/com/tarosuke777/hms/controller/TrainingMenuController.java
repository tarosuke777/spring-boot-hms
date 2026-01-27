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
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/trainingMenu")
@RequiredArgsConstructor
public class TrainingMenuController {

	private final TrainingMenuService trainingMenuService;

	@GetMapping("/list")
	public String getList(Model model, @AuthenticationPrincipal UserDetails UserDetail) {
		model.addAttribute("trainingMenuList",
				trainingMenuService.getTrainingMenuList(UserDetail.getUsername()));
		model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
		return "trainingMenu/list";
	}

	@GetMapping("/register")
	public String getRegister(@ModelAttribute TrainingMenuForm form, Model model) {
		model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
		return "trainingMenu/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute @Validated TrainingMenuForm form,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return getRegister(form, model);
		}
		trainingMenuService.registerTrainingMenu(form);
		return "redirect:/trainingMenu/list";
	}

	@GetMapping("/detail/{trainingMenuId}")
	public String getDetail(@PathVariable("trainingMenuId") Integer trainingMenuId, Model model,
			@AuthenticationPrincipal UserDetails user) {
		TrainingMenuForm form =
				trainingMenuService.getTrainingMenuDetails(trainingMenuId, user.getUsername());
		model.addAttribute("trainingMenuForm", form);
		model.addAttribute("targetAreaMap", TargetArea.getTargetAreaMap());
		return "trainingMenu/detail";
	}

	@PostMapping(value = "detail", params = "update")
	public String update(@ModelAttribute @Validated(UpdateGroup.class) TrainingMenuForm form,
			BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails user) {
		if (bindingResult.hasErrors()) {
			return "trainingMenu/detail";
		}
		trainingMenuService.updateTrainingMenu(form, user.getUsername());
		return "redirect:/trainingMenu/list";
	}

	@PostMapping(value = "/detail", params = "delete")
	public String delete(TrainingMenuForm form, Model model,
			@AuthenticationPrincipal UserDetails user) {
		trainingMenuService.deleteTrainingMenu(form.getTrainingMenuId(), user.getUsername());
		return "redirect:/trainingMenu/list";
	}
}
