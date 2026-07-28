package com.tarosuke777.hms.controller;

import com.tarosuke777.hms.enums.TaskStatus;
import com.tarosuke777.hms.form.TaskForm;
import com.tarosuke777.hms.security.LoginUser;
import com.tarosuke777.hms.service.TaskService;
import com.tarosuke777.hms.validation.UpdateGroup;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/task")
public class TaskController {

  private static final String REDIRECT_LIST = "redirect:/task/list";
  private static final String LIST_VIEW = "task/list";
  private static final String REGISTER_VIEW = "task/register";
  private static final String SESSION_STATUS_KEY = "selectedTaskStatus";

  private final TaskService taskService;

  @GetMapping("/list")
  public String list(@RequestParam(required = false) TaskStatus status, Model model,
      @AuthenticationPrincipal LoginUser user, HttpSession session) {

    if (status != null) {
      // statusが存在すればセッションに保持
      session.setAttribute(SESSION_STATUS_KEY, status);
    } else {
      // statusが無ければセッションをクリア（初期化）
      session.removeAttribute(SESSION_STATUS_KEY);
    }

    model.addAttribute("tasks", taskService.getTaskList(user.getId(), status));
    model.addAttribute("status", status);
    return LIST_VIEW;
  }

  @GetMapping("/register")
  public String register(@ModelAttribute TaskForm taskForm) {
    return REGISTER_VIEW;
  }

  @PostMapping("/create")
  public String create(@Validated @ModelAttribute TaskForm taskForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return REGISTER_VIEW;
    }

    taskService.createTask(taskForm);

    return REDIRECT_LIST;
  }

  @PostMapping("/update")
  public String update(@Validated(UpdateGroup.class) @ModelAttribute TaskForm taskForm,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user,
      HttpSession session, RedirectAttributes redirectAttributes) {

    TaskStatus currentStatus = (TaskStatus) session.getAttribute(SESSION_STATUS_KEY);

    if (bindingResult.hasErrors()) {
      // エラー時は一覧を再取得して戻る
      model.addAttribute("status", currentStatus);
      model.addAttribute("tasks", taskService.getTaskList(user.getId(), currentStatus));
      return LIST_VIEW;
    }

    // ServiceのupdateTask(TaskForm)を呼び出す
    taskService.updateTask(taskForm, user.getId());

    if (currentStatus != null) {
      redirectAttributes.addAttribute("status", currentStatus);
    }

    return REDIRECT_LIST;
  }

  @PostMapping("/delete")
  public String delete(@RequestParam("id") Integer id, @AuthenticationPrincipal LoginUser user,
      HttpSession session, RedirectAttributes redirectAttributes) {

    TaskStatus currentStatus = (TaskStatus) session.getAttribute(SESSION_STATUS_KEY);

    if (currentStatus != null) {
      redirectAttributes.addAttribute("status", currentStatus);
    }

    taskService.deleteTask(Objects.requireNonNull(id), user.getId());
    return REDIRECT_LIST;
  }
}
