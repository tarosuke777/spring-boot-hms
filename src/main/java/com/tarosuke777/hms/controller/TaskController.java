package com.tarosuke777.hms.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tarosuke777.hms.domain.TaskService;
import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.form.TaskForm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "task/list";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute TaskForm taskForm) {
        return "task/register";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute TaskForm taskForm, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "task/register";
        }

        TaskEntity taskEntity = new TaskEntity();
        BeanUtils.copyProperties(taskForm, taskEntity);
        taskService.create(taskEntity);

        return "redirect:/task/list";
    }

    @PostMapping("/update")
    public String update(@Validated @ModelAttribute TaskForm taskForm, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tasks", taskService.findAll());
            return "task/list";
        }

        TaskEntity taskEntity = new TaskEntity();
        BeanUtils.copyProperties(taskForm, taskEntity);
        taskService.update(taskEntity);

        return "redirect:/task/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id) {
        taskService.delete(id);
        return "redirect:/task/list";
    }
}
