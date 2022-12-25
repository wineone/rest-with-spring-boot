package br.com.wineone.securityJwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.wineone.data.vo.v1.security.TokenVO;
import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenProvider {
	
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-lenght:3600000}")
	private long validityInMiliseconds = 3600000; // 1h
	
	@Autowired
	private UserDetailsService userDetaisService;
	
	private Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenVO createAccessToken(String username, List<String> roles) {
		Date dateNow = new Date();
		Date valid = new Date(dateNow.getTime() + validityInMiliseconds);
		
		var accessToken = getAccessToken(username, roles, dateNow, valid);
		var refreshToken = getAccessToken(username, roles, dateNow);
		return new TokenVO(username, true, dateNow, valid, accessToken, refreshToken);
	}

	private String getAccessToken(String username, List<String> roles, Date dateNow) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAccessToken(String username, List<String> roles, Date dateNow, Date valid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
