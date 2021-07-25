package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SareetaApplicationTests {

	@Test
	public void contextLoads() {
	}

	private UserController userController;
	private UserRepository userRepo = mock(UserRepository.class);
	private CartRepository cartRepo = mock(CartRepository.class);
	private BCryptPasswordEncoder encoder= mock(BCryptPasswordEncoder.class);
	
	private CartController cartController;
	private ItemRepository itemRepo = mock(ItemRepository.class);
	
	@Before 
	public void setup() {
		userController = new UserController();
		TestUtils.injectObjects(userController, "userRepository", userRepo);
		TestUtils.injectObjects(userController, "cartRepository", cartRepo);
		TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
		cartController = new CartController();
		TestUtils.injectObjects(cartController, "userRepository", userRepo);
		TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
	}
	
	@Test
	public void createUserSuccessfullyCase() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("123456789");
		request.setPassword2("123456789");
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User user = (User) response.getBody();
		assertNotNull(user);
		assertNotNull(user.getId());
	}
	
	@Test
	public void createUserFailureCaseMisMatchPasswords() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("123456");
		request.setPassword2("123456789");
		
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(500, response.getStatusCodeValue());
		String error = (String) response.getBody();
		assertEquals(error, "Provided passwords are not matched");
	}
	
	@Test
	public void createUserFailureCasePasswordLengthIssue() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("123456");
		request.setPassword2("123456");
		
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(500, response.getStatusCodeValue());
		String error = (String) response.getBody();
		assertEquals(error, "password length must be higher than 8 characters");
	}
}
