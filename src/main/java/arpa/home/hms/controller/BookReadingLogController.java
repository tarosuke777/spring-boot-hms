package arpa.home.hms.controller;

import arpa.home.hms.exception.IllegalRequestException;
import arpa.home.hms.form.BookReadingLogForm;
import arpa.home.hms.security.LoginUser;
import arpa.home.hms.service.BookReadingLogService;
import arpa.home.hms.service.BookService;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@Controller
@Slf4j
@RequestMapping("/bookReadingLog")
@RequiredArgsConstructor
public class BookReadingLogController {

  private static final String REDIRECT_LIST = "redirect:/bookReadingLog/list";
  private static final String LIST_VIEW = "bookReadingLog/list";
  private static final String DETAIL_VIEW = "bookReadingLog/detail";
  private static final String REGISTER_VIEW = "bookReadingLog/register";

  private final BookReadingLogService bookReadingLogService;
  private final BookService bookService;

  @GetMapping("/list")
  public String getList(@PageableDefault(size = 10) Pageable pageable, Model model,
      @AuthenticationPrincipal LoginUser user) {
    Page<BookReadingLogForm> bookReadingLogPage =
        bookReadingLogService.getBookReadingLogList(user.getId(), pageable);
    Map<Integer, String> bookMap = bookService.getBookMap(user.getId());

    model.addAttribute("bookReadingLogPage", bookReadingLogPage);
    model.addAttribute("bookMap", bookMap);
    return LIST_VIEW;
  }

  @GetMapping("/detail/{id}")
  public String getDetail(@PathVariable("id") Integer id, Model model,
      @AuthenticationPrincipal LoginUser user) {
    BookReadingLogForm bookReadingLogForm =
        bookReadingLogService.getBookReadingLogDetails(id, user.getId());
    model.addAttribute("bookReadingLogForm", bookReadingLogForm);
    model.addAttribute("bookMap", bookService.getBookMap(user.getId()));
    return DETAIL_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute BookReadingLogForm bookReadingLogForm, Model model,
      @AuthenticationPrincipal LoginUser user) {
    if (bookReadingLogForm.getReadDate() == null) {
      bookReadingLogForm.setReadDate(LocalDate.now(ZoneId.systemDefault()));
    }
    model.addAttribute("bookMap", bookService.getBookMap(user.getId()));
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated BookReadingLogForm form,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("bookMap", bookService.getBookMap(user.getId()));
      return REGISTER_VIEW;
    }

    bookReadingLogService.registerBookReadingLog(form, user.getId());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) BookReadingLogForm form,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal LoginUser user) {

    if (bindingResult.hasFieldErrors(BookReadingLogForm.Fields.id)
        || bindingResult.hasFieldErrors(BookReadingLogForm.Fields.version)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    if (bindingResult.hasErrors()) {
      model.addAttribute("bookMap", bookService.getBookMap(user.getId()));
      return DETAIL_VIEW;
    }

    bookReadingLogService.updateBookReadingLog(form, user.getId());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) BookReadingLogForm form,
      BindingResult bindingResult, @AuthenticationPrincipal LoginUser user) {

    if (bindingResult.hasFieldErrors(BookReadingLogForm.Fields.id)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    bookReadingLogService.deleteBookReadingLog(Objects.requireNonNull(form.getId()), user.getId());
    return REDIRECT_LIST;
  }
}
