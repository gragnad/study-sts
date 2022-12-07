package com.nalsstudio.config.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoEncodingPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		// TODO Auto-generated method stub
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPawword, String encodedPassword) {
		// TODO Auto-generated method stub
		return rawPawword.toString().equals(encodedPassword);
	}

}
