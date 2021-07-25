package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Object> createUser(@RequestBody CreateUserRequest createUserRequest) {
		if(createUserRequest.getPassword1().isEmpty()) {
			log.error("Error Creating user "+createUserRequest.getUsername()+ ": Password cann't be empty");
			return new ResponseEntity<Object>("Password cann't be empty",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(!createUserRequest.getPassword1().equals(createUserRequest.getPassword2())) {
			log.error("Error Creating user "+createUserRequest.getUsername()+ ": Provided passwords are not matched");
			return new ResponseEntity<Object>("Provided passwords are not matched",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(createUserRequest.getPassword1().length()<=8) {
			log.error("Error Creating user "+createUserRequest.getUsername()+ ": password length must be higher than 8 characters");
			return new ResponseEntity<Object>("password length must be higher than 8 characters",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword1()));
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		log.info("User "+ createUserRequest.getUsername()+" has been created succesfully");
		return ResponseEntity.ok(user);
	}
	
}
