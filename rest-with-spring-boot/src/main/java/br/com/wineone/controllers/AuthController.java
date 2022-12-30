package br.com.wineone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wineone.data.vo.v1.security.AccountCredentialsVO;
import br.com.wineone.services.AuthServices;
import io.swagger.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

	
	@Autowired
	private AuthServices authServices; 
	
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user in the application")
	@PostMapping(value = "/signin")
	public ResponseEntity signin(@RequestBody() AccountCredentialsVO data){
		if(data == null || data.getUsername() == null || 
		   data.getUsername().equals("") || data.getPassword() == null || 
		   data.getPassword().equals("")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username and password must be provided!");
		}else {
			var token = authServices.signin(data);
			if(token == null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username and password must be provided!");
			}else {
				return token;
			}
			
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Refresh token for authenticated user and return a new token")
	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken){
		if(username == null || username.equals("") || refreshToken == null || refreshToken.equals("")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username and token must be provided!");
		}else {
			var token = authServices.refreshToken(username,refreshToken);
			if(token == null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username and password must be provided!");
			}else {
				return token;
			}
			
		}
	}
}








