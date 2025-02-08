package com.tarosuke777.hms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.domain.AuthorService;
import com.tarosuke777.hms.domain.BookService;
import com.tarosuke777.hms.form.BookForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

  private static final String REDIRECT_LIST = "redirect:/book/list";
  private static final String LIST_VIEW = "book/list";
  private static final String DETAIL_VIEW = "book/detail";
  private static final String REGISTER_VIEW = "book/register";

  private final BookService bookService;
  private final AuthorService authorService;

  @GetMapping("/list")
  public String getList(Model model) {

    List<BookForm> bookList = bookService.getBookList();
    Map<Integer, String> authorMap = authorService.getAuthorMap();

    addAttributesToModel(model, bookList, authorMap);

    return LIST_VIEW;
  }

  @GetMapping("/detail/{bookId}")
  public String getDetail(@PathVariable("bookId") Integer bookId, Model model) {

    BookForm bookForm = bookService.getBookDetails(bookId);
    Map<Integer, String> authorMap = authorService.getAuthorMap();

    addAttributesToModel(model, bookForm, authorMap);

    return DETAIL_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(BookForm bookForm, Model model) {

    Map<Integer, String> authorMap = authorService.getAuthorMap();

    addAttributesToModel(model, authorMap);

    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(
      @ModelAttribute @Validated BookForm form, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }

    bookService.registerBook(form);

    return REDIRECT_LIST;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(BookForm form) {

    bookService.updateBook(form);

    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(BookForm form, Model model) {

    bookService.deleteBook(form.getBookId());

    return REDIRECT_LIST;
  }

  private void addAttributesToModel(
      Model model, List<BookForm> bookList, Map<Integer, String> authorMap) {
    model.addAttribute("authorMap", authorMap);
    model.addAttribute("bookList", bookList);
  }

  private void addAttributesToModel(
      Model model, BookForm bookForm, Map<Integer, String> authorMap) {
    model.addAttribute("bookForm", bookForm);
    model.addAttribute("authorMap", authorMap);
  }

  private void addAttributesToModel(Model model, Map<Integer, String> authorMap) {
    model.addAttribute("authorMap", authorMap);
  }
}
