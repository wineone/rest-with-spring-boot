package br.com.wineone.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.wineone.security.Jwt.JwtConfigurer;
import br.com.wineone.security.Jwt.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
 

	@Autowired
    private final JwtTokenProvider jwtTokenProvider;
 
	public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
		super();
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .requestMatchers("/users").denyAll()
                )
                .cors()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .build();
    }
 
	@Bean
	public PasswordEncoder passwordEncoder() {
		Map<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());
		return passwordEncoder;
	}
 
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}