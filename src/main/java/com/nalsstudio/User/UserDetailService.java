package com.nalsstudio.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailService {
	UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException;
}
