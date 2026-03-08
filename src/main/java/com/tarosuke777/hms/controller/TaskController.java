package com.tarosuke777.hms.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tarosuke777.hms.form.TaskForm;
import com.tarosuke777.hms.security.LoginUser;
import com.tarosuke777.hms.service.TaskService;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/task")
public class TaskController {

    private static final String REDIRECT_LIST = "redirect:/task/list";
    private static final String LIST_VIEW = "task/list";
    private static final String REGISTER_VIEW = "task/register";

    private final TaskService taskService;

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal LoginUser user) {
        model.addAttribute("tasks", taskService.getTaskList(user.getId()));
        return LIST_VIEW;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute TaskForm taskForm) {
        return REGISTER_VIEW;
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute TaskForm taskForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }

        taskService.createTask(taskForm);

        return REDIRECT_LIST;
    }

    @PostMapping("/update")
    public String update(@Validated(UpdateGroup.class) @ModelAttribute TaskForm taskForm,
            BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {
        if (bindingResult.hasErrors()) {
            // エラー時は一覧を再取得して戻る
            model.addAttribute("tasks", taskService.getTaskList(user.getId()));
            return LIST_VIEW;
        }

        // ServiceのupdateTask(TaskForm)を呼び出す
        taskService.updateTask(taskForm, user.getId());

        return REDIRECT_LIST;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id, @AuthenticationPrincipal LoginUser user) {
        taskService.deleteTask(id, user.getId());
        return REDIRECT_LIST;
    }
}
