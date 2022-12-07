package com.nalsstudio.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	//@Autowired DataSource datasource;
	@Autowired UserDetailsService userDetailservice;
	
	@Bean
	protected PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 폼 로그인 기능과 베이직 인증 비활성화
        http.formLogin().disable()
                .httpBasic().disable();

        // CORS 사용자 설정
        http.cors().disable();

        // CSRF 방지 지원 기능 비활성화
        http.csrf().disable();

        // 윕 경로 보안 설정
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").permitAll()
//                .antMatchers("/").access("hasRole('ROLE_USER')")
                .antMatchers("/api/**").access("hasRole('ROLE_USER')")
                .antMatchers("/commonApi/**").access("permitAll")
                .antMatchers("/**").access("permitAll")
                .anyRequest().authenticated()
                .and().httpBasic();
		
	}

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("user1")
//			.password("{noop}password1")
//			.authorities("ROLE_USER")
//			.and()
//			.withUser("user2")
//			.password("{noop}password2")
//			.authorities("ROLE_USER");
		
		/* JDBC USER STORE */
//		String userQuery = "select username, password, enabled from users where username=?";
//		String authorityQuery = "select username, suthority from authorities where username=?";
//		auth
//			.jdbcAuthentication()
//			.dataSource(datasource)
//			.usersByUsernameQuery(userQuery)
//			.authoritiesByUsernameQuery(authorityQuery)
//			//.passwordEncoder(new BCryptPasswordEncoder());
//			.passwordEncoder(new NoEncodingPasswordEncoder());
		
		/* service logic process */
		auth
			.userDetailsService(userDetailservice)
			.passwordEncoder(encoder());
		
		
		
		
	}

}
