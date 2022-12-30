package br.com.wineone.security.Jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.com.wineone.data.vo.v1.security.TokenVO;
import br.com.wineone.exceptions.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {
	
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-length:3600000}")
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
		var refreshToken = getRefreshToken(username, roles, dateNow);
		return new TokenVO(username, true, dateNow, valid, accessToken, refreshToken);
	}
	
	public TokenVO createAccessTokenWithRefresh(String username, List<String> roles, String refresh) {
		Date dateNow = new Date();
		Date valid = new Date(dateNow.getTime() + validityInMiliseconds);
		
		var accessToken = getAccessToken(username, roles, dateNow, valid);
		return new TokenVO(username, true, dateNow, valid, accessToken, refresh);
	}
	
	public TokenVO refreshToken(String refeshToken, String usernameCome) {
		refeshToken = refeshToken.replace("Bearer ", "");
		
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJwt = verifier.verify(refeshToken);
		
		String username = decodedJwt.getSubject();
		
		if(!username.equals(usernameCome))
			throw new InvalidJwtAuthenticationException("username not matches with jwt");
		List<String> roles = decodedJwt.getClaim("roles").asList(String.class);
		
		return createAccessTokenWithRefresh(username, roles, refeshToken);
	}


	private String getAccessToken(String username, List<String> roles, Date dateNow, Date valid) {
		String issuerUrl = ServletUriComponentsBuilder.
												fromCurrentContextPath().
												build().
												toUriString();
		return JWT.create().
				withClaim("roles", roles).
				withIssuedAt(dateNow).
				withExpiresAt(valid).
				withSubject(username).
				withIssuer(issuerUrl).
				sign(algorithm).
				strip();
				
				
	}
	
	private String getRefreshToken(String username, List<String> roles, Date dateNow) {
		Date validityRefreshToken = new Date(dateNow.getTime() + (validityInMiliseconds) * 3); // 3h
		return JWT.create().
				withClaim("roles", roles).
				withIssuedAt(dateNow).
				withExpiresAt(validityRefreshToken).
				withSubject(username).
				sign(algorithm).
				strip();
	}
	
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		UserDetails userDetails = this.userDetaisService.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		var alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
		try {			
			DecodedJWT decodedJWT = verifier.verify(token);
			return decodedJWT;
		}catch (Exception e) {
			return null;
		}
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.replace("Bearer ", "");
		}
		
		return null;
	}
	
	
	public boolean validateToken(String token) {
		DecodedJWT decToken = decodedToken(token);
		if(decToken == null) {
			throw new InvalidJwtAuthenticationException("Invalid token jwt");
		}
		System.out.println(decToken.getExpiresAt());
		System.out.println(new Date());
		return true;
	}
}

























