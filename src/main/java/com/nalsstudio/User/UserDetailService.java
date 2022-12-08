package com.nalsstudio.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nalsstudio.domain.Authorities;
import com.nalsstudio.domain.User;
import com.nalsstudio.dto.UserDto;

public interface UserDetailService {
	UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException;
	
	User loginUser(String name, String password);
	
	public Authorities registerRole(User user, String role);
}
