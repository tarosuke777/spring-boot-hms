package com.tarosuke777.hms.security;

import com.tarosuke777.hms.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserDetailServiceImpl userDetailsService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
        && LoginUser.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null)
      return null;

    Object principal = auth.getPrincipal();

    // すでに LoginUser の場合（フォーム認証など）
    if (principal instanceof LoginUser loginUser) {
      return loginUser;
    }

    // WebAuthn 認証の場合
    if (principal instanceof PublicKeyCredentialUserEntity userEntity) {
      return userDetailsService.loadUserByUsername(userEntity.getName());
    }

    return null;
  }
}
