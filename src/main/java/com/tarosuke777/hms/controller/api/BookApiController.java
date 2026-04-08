package com.tarosuke777.hms.controller.api;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tarosuke777.hms.form.BookForm;
import com.tarosuke777.hms.security.LoginUser;
import com.tarosuke777.hms.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    @GetMapping
    public List<BookForm> getBooks(@AuthenticationPrincipal LoginUser user) {
        return bookService.getBookList(user.getId(), null, null, Pageable.unpaged()).getContent();
    }
}
