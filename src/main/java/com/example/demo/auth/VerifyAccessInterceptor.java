package com.example.demo.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class VerifyAccessInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.debug("preHandle URI:[{}]", request.getRequestURI());
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      log.warn("auth is null");
      return true;
    }
    if (auth.isAuthenticated()) {
      log.debug("auth name:[{}]", auth.getName());
      if (auth instanceof UsernamePasswordAuthenticationToken) {
        Object principal = auth.getPrincipal();
        if (principal != null) {
          log.debug("principal:{}, class:{}", ((SimpleLoginUser) principal).toString(), principal.getClass().getCanonicalName());
        } else {
          log.debug("principal is null");
        }
        Object credentials = auth.getCredentials();
        if (credentials != null) {
          log.debug("credentials:{}, class:{}", (String)credentials, credentials.getClass().getCanonicalName());
        } else {
          log.debug("credentials is null");
        }
      }
    }
    return true;
  }

}
