package com.tarosuke777.hms.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.tarosuke777.hms.form.TaskForm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("tasks", taskService.getTaskList(user.getUsername()));
        return "task/list";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute TaskForm taskForm) {
        return "task/register";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute TaskForm taskForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "task/register";
        }

        taskService.createTask(taskForm);

        return "redirect:/task/list";
    }

    @PostMapping("/update")
    public String update(@Validated @ModelAttribute TaskForm taskForm, BindingResult bindingResult,
            Model model, @AuthenticationPrincipal UserDetails user) {
        if (bindingResult.hasErrors()) {
            // エラー時は一覧を再取得して戻る
            model.addAttribute("tasks", taskService.getTaskList(user.getUsername()));
            return "task/list";
        }

        // ServiceのupdateTask(TaskForm)を呼び出す
        taskService.updateTask(taskForm, user.getUsername());

        return "redirect:/task/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id,
            @AuthenticationPrincipal UserDetails user) {
        taskService.deleteTask(id, user.getUsername());
        return "redirect:/task/list";
    }
}
