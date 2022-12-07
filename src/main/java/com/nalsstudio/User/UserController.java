package com.nalsstudio.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nalsstudio.domain.User;
import com.nalsstudio.dto.RegistrationForm;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private UserDetailService userDetailService;
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	
	public UserController(UserDetailService userDetailService, UserRepository userRepo, PasswordEncoder passwordEncoder) {
		super();
		this.userDetailService = userDetailService;
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
		
	
	@PostMapping(value = "/register")
	public ResponseEntity<User> processRegistraion(RegistrationForm form) {
		User user = userRepo.save(form.toUser(passwordEncoder));
		if(user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);	
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);	
	}
	
}
