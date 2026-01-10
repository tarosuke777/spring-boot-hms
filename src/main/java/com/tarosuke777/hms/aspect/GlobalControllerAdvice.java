package com.tarosuke777.hms.aspect;

import java.util.UUID;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler({Exception.class})
	public String handleException(Exception e, Model model) {
		// ユニークなIDを発行
		String errorId = UUID.randomUUID().toString();

		// ログにはスタックトレースとIDを紐づけて出力（開発者はこれを見る）
		log.error("Error ID: {}", errorId, e);

		// 画面にはIDとクラス名程度に留める
		model.addAttribute("errorId", errorId);
		model.addAttribute("exception", e.getClass().getSimpleName());
		model.addAttribute("isNotDisplayMenu", true);

		return "error";
	}

}
