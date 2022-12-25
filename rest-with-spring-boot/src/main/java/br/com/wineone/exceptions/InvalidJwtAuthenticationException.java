package br.com.wineone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {
	

	private static final long serialVersionUID = 1;
	
	public InvalidJwtAuthenticationException(String msg) {
		super(msg);
	}
	
}
