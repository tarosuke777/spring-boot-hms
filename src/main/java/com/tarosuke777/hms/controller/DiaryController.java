package com.tarosuke777.hms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.domain.DiaryService;
import com.tarosuke777.hms.form.DiaryForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private static final String REDIRECT_LIST = "redirect:/diary/list";
    private static final String LIST_VIEW = "diary/list";
    private static final String DETAIL_VIEW = "diary/detail";
    private static final String REGISTER_VIEW = "diary/register";

    private final DiaryService diaryService;

    @GetMapping("/list")
    public String getList(Model model) {
        List<DiaryForm> diaryList = diaryService.getDiaryList();
        model.addAttribute("diaryList", diaryList);
        return LIST_VIEW;
    }

    @GetMapping("/detail/{diaryId}")
    public String getDetail(@PathVariable("diaryId") Integer diaryId, Model model) {
        DiaryForm diaryForm = diaryService.getDiaryDetails(diaryId);
        model.addAttribute("diaryForm", diaryForm);
        return DETAIL_VIEW;
    }

    @GetMapping("/register")
    public String getRegister(DiaryForm diaryForm, Model model) {
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Validated DiaryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return getRegister(form, model);
        }
        diaryService.registerDiary(form);
        return REDIRECT_LIST;
    }

    @PostMapping(value = "/detail", params = "update")
    public String update(DiaryForm form) {
        diaryService.updateDiary(form);
        return REDIRECT_LIST;
    }

    @PostMapping(value = "/detail", params = "delete")
    public String delete(DiaryForm form) {
        diaryService.deleteDiary(form.getDiaryId());
        return REDIRECT_LIST;
    }
}