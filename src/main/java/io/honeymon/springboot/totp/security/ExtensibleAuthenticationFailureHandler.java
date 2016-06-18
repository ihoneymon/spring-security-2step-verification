package io.honeymon.springboot.totp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtensibleAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.debug("request: {}, response: {}, ", request, response, exception.getClass().getTypeName());
		if (exception instanceof BadCredentialsException) {
			response.sendRedirect("/login?error=" + exception.getMessage());
		} else if (exception instanceof InternalAuthenticationServiceException) {
			response.sendRedirect("/login?error=" + exception.getMessage());
		}
	}

}
