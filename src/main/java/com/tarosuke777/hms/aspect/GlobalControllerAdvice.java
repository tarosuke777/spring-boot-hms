package com.tarosuke777.hms.aspect;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler({ Exception.class })
	public String testExceptionHandle(Exception e, Model model) {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		model.addAttribute("exception", e.getClass());
		model.addAttribute("stackTrace", stackTrace);
		
		model.addAttribute("isNotDisplayMenu", true);
		return "error";
	}

}
