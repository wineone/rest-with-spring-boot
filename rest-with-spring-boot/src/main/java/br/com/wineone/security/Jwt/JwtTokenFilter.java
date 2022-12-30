package br.com.wineone.security.Jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import br.com.wineone.exceptions.InvalidJwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends GenericFilterBean {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		super();
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
			if(token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				if(auth != null) {
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			chain.doFilter(request, response);
		} catch (InvalidJwtAuthenticationException e) {
			HttpServletResponse respondeCast = ((HttpServletResponse) response);
			respondeCast.setStatus(403);
			respondeCast.setHeader("Content-Type", "application/json");
			respondeCast.getWriter().write("{\"message\": \"Invalid token jwt\"}");
			respondeCast.getWriter().flush();
		}
	}
}









