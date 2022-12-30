package br.com.wineone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.wineone.data.vo.v1.security.AccountCredentialsVO;
import br.com.wineone.data.vo.v1.security.TokenVO;
import br.com.wineone.repositories.UserRepository;
import br.com.wineone.security.Jwt.JwtTokenProvider;

@Service
public class AuthServices {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public ResponseEntity<TokenVO> signin(AccountCredentialsVO data) {
		try {
			var username = data.getUsername();
			var password = data.getPassword();
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			var user = userRepository.findByUsername(username);
			
			var tokenVO = new TokenVO();
			if(user == null) {
				throw new UsernameNotFoundException("usuario não encontrado");
			}else {
				tokenVO = tokenProvider.createAccessToken(username, user.getRoles());
			}
			
			return ResponseEntity.ok(tokenVO);
		}catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password");
		}
	}
	
	public ResponseEntity<TokenVO> refreshToken(String username, String refreshToken) {
		var user = userRepository.findByUsername(username);
		
		var tokenVO = new TokenVO();
		if(user == null) {
			throw new UsernameNotFoundException("usuario não encontrado");
		}else {
			tokenVO = tokenProvider.refreshToken(refreshToken, username);
		}
		
		return ResponseEntity.ok(tokenVO);
	}
	
}








