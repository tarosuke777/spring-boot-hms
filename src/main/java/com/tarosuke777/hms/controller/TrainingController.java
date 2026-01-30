package com.tarosuke777.hms.controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import com.tarosuke777.hms.domain.TrainingMenuService;
import com.tarosuke777.hms.domain.TrainingService;
import com.tarosuke777.hms.enums.TargetArea;
import com.tarosuke777.hms.form.SelectOptionTrainingMenu;
import com.tarosuke777.hms.form.TrainingForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

	private static final String REDIRECT_LIST = "redirect:/training/list";
	private static final String LIST_VIEW = "training/list";
	private static final String DETAIL_VIEW = "training/detail";
	private static final String REGISTER_VIEW = "training/register";

	private final TrainingService trainingService;
	private final TrainingMenuService trainingMenuService;

	@GetMapping("/list")
	public String getList(Model model,
			@RequestParam(name = "orderBy", defaultValue = "trainingDate") String orderBy,
			@RequestParam(name = "sort", defaultValue = "desc") String sort,
			@AuthenticationPrincipal UserDetails user) {

		List<TrainingForm> trainingList =
				trainingService.getTrainingList(orderBy, sort, user.getUsername());
		Map<Integer, String> trainingAreaMap = TargetArea.getTargetAreaMap();
		Map<Integer, String> trainingMenuMap = trainingMenuService.getTrainingMenuMap();

		addAttributesToModel(model, trainingList, trainingMenuMap, trainingAreaMap);

		return LIST_VIEW;
	}

	@GetMapping("/detail/{trainingId}")
	public String getDetail(@PathVariable("trainingId") Integer trainingId, Model model,
			@AuthenticationPrincipal UserDetails user) {

		TrainingForm trainingForm =
				trainingService.getTrainingDetails(trainingId, user.getUsername());
		Map<Integer, String> trainingMenuMap = trainingMenuService.getTrainingMenuMap();

		addAttributesToModel(model, trainingForm, trainingMenuMap);

		return DETAIL_VIEW;
	}

	@GetMapping("/register")
	public String getRegister(TrainingForm trainingForm, Model model) {

		List<SelectOptionTrainingMenu> trainingMenuSelectList =
				trainingMenuService.getTrainingMenuSelectList();
		Map<Integer, String> trainingTargetAreaMap = TargetArea.getTargetAreaMap();

		addAttributesToModel(model, trainingMenuSelectList, trainingTargetAreaMap);

		return REGISTER_VIEW;
	}

	@PostMapping("/register")
	public String register(@ModelAttribute @Validated TrainingForm form,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return getRegister(form, model);
		}

		trainingService.registerTraining(form);

		return REDIRECT_LIST;
	}

	@PostMapping(value = "detail", params = "update")
	public String update(TrainingForm form, @AuthenticationPrincipal UserDetails user) {

		trainingService.updateTraining(form, user.getUsername());

		return REDIRECT_LIST;
	}

	@PostMapping(value = "/detail", params = "delete")
	public String delete(TrainingForm form, Model model,
			@AuthenticationPrincipal UserDetails user) {

		trainingService.deleteTraining(form.getTrainingId(), user.getUsername());

		return REDIRECT_LIST;
	}

	private void addAttributesToModel(Model model, List<TrainingForm> trainingList,
			Map<Integer, String> trainingMenuMap, Map<Integer, String> trainingAreaMap) {
		model.addAttribute("trainingAreaMap", trainingAreaMap);
		model.addAttribute("trainingMenuMap", trainingMenuMap);
		model.addAttribute("trainingList", trainingList);
	}

	private void addAttributesToModel(Model model, TrainingForm trainingForm,
			Map<Integer, String> trainingMenuMap) {
		model.addAttribute("trainingForm", trainingForm);
		model.addAttribute("trainingMenuMap", trainingMenuMap);
	}

	private void addAttributesToModel(Model model,
			List<SelectOptionTrainingMenu> trainingMenuSelectList,
			Map<Integer, String> trainingTargetAreaMap) {
		model.addAttribute("trainingMenuSelectList", trainingMenuSelectList);
		model.addAttribute("trainingTargetAreaMap", trainingTargetAreaMap);
	}
}
