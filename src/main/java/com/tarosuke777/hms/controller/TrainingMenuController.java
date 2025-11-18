package com.tarosuke777.hms.controller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.enums.TargetArea;
import com.tarosuke777.hms.form.TrainingMenuForm;
import com.tarosuke777.hms.repository.TrainingMenuMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/trainingMenu")
public class TrainingMenuController {

	@Autowired
	private TrainingMenuMapper trainingMenuMapper;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/list")
	public String getList(Model model) {

		List<TrainingMenuForm> trainingMenuList = trainingMenuMapper.findMany().stream()
				.map(entity -> modelMapper.map(entity, TrainingMenuForm.class)).toList();

		model.addAttribute("trainingMenuList", trainingMenuList);

		Map<Integer, String> targetAreaMap = TargetArea.getTargetAreaMap();

		model.addAttribute("targetAreaMap", targetAreaMap);

		return "trainingMenu/list";
	}

	@GetMapping("/register")
	public String getRegister(TrainingMenuForm form, Model model) {

		model.addAttribute("trainingMenuForm", form);

		Map<Integer, String> targetAreaMap = TargetArea.getTargetAreaMap();

		model.addAttribute("targetAreaMap", targetAreaMap);

		return "trainingMenu/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute @Validated TrainingMenuForm form,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return getRegister(form, model);
		}

		trainingMenuMapper.insertOne(form.getTrainingMenuName(), form.getTargetAreaId(),
				form.getLink(), form.getMaxWeight(), form.getMaxReps(), form.getMaxSets());

		return "redirect:/trainingMenu/list";
	}

	@GetMapping("/detail/{trainingMenuId}")
	public String getDetail(TrainingMenuForm form, Model model,
			@PathVariable("trainingMenuId") Integer trainingMenuId) {

		TrainingMenuEntity trainingMenu = trainingMenuMapper.findOne(trainingMenuId);

		form = modelMapper.map(trainingMenu, TrainingMenuForm.class);

		model.addAttribute("trainingMenuForm", form);

		Map<Integer, String> targetAreaMap = TargetArea.getTargetAreaMap();

		model.addAttribute("targetAreaMap", targetAreaMap);

		return "trainingMenu/detail";
	}

	@PostMapping(value = "detail", params = "update")
	public String update(TrainingMenuForm form) {

		trainingMenuMapper.updateOne(form.getTrainingMenuId(), form.getTrainingMenuName(),
				form.getTargetAreaId(), form.getLink(), form.getMaxWeight(), form.getMaxReps(),
				form.getMaxSets());

		return "redirect:/trainingMenu/list";
	}

	@PostMapping(value = "/detail", params = "delete")
	public String delete(TrainingMenuForm form, Model model) {

		trainingMenuMapper.deleteOne(form.getTrainingMenuId());

		return "redirect:/trainingMenu/list";
	}
}
