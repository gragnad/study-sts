package com.nalsstudio.User;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nalsstudio.domain.Authorities;
import com.nalsstudio.domain.User;
import com.nalsstudio.dto.AuthoritiesDto;
import com.nalsstudio.dto.UserDto;
import com.nalsstudio.utils.ChipherUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private static String TAG = UserController.class.getName();
	private UserDetailService userService;
	
	public UserController(UserDetailService userService)  {
		this.userService = userService;
	}

	@PostMapping(value = "/register")
	public ResponseEntity<User> processRegistraion(HttpServletRequest req, 
			HttpServletResponse res,
			@RequestBody UserDto userDto) {
		User user = userDto.toUser();
		Authorities auth = userService.registerRole(user, "ROLE_USER");
		if(auth != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);	
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);	
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<Object> loginUser(@RequestBody UserDto userDto) {
		try {
			String encPassword = ChipherUtil.encryptPassword(userDto.getPassword(), userDto.getUsername());
			User user = userService.loginUser(userDto.getUsername(), encPassword);
			if(user != null) {
				return ResponseEntity.status(HttpStatus.OK).body(user);
			}
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		}
	}
	
}
