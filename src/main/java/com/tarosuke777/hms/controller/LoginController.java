package com.tarosuke777.hms.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {

	@GetMapping("/login")
	public String login(Model model) {

		// 現在の認証情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 認証情報が存在し、匿名ユーザーではない（＝ログイン済み）ことを確認
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken)) {
			log.info("既にログイン済みのため、ログイン成功時のデフォルトURLへリダイレクトします。");
			return "redirect:/task/list";
		}

		model.addAttribute("isNotDisplayMenu", true);
		return "login";
	}
}
