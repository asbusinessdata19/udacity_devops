package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SareetaApplicationTests {
	

	@Test
	public void contextLoads() {
	}

	private UserController userController;
	private CartController cartController;
	private ItemController itemController;
	private OrderController orderController;
	
	private UserRepository userRepo = mock(UserRepository.class);
	private CartRepository cartRepo = mock(CartRepository.class);
	private BCryptPasswordEncoder encoder= mock(BCryptPasswordEncoder.class);
	private OrderRepository orderRepo = mock(OrderRepository.class);
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
		itemController= new ItemController();
		TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
		orderController= new OrderController();
		TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
		TestUtils.injectObjects(orderController, "userRepository", userRepo);
	}
	
	@Test
	public void createUserSuccessfullyCase() throws Exception{
		when(encoder.encode("123456789")).thenReturn("123456789");
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("123456789");
		request.setPassword2("123456789");
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User user = (User) response.getBody();
		System.out.println(user);
		user.setId(null);
		user.setUsername("lasaleh");
		response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		user = (User) response.getBody();
		System.out.println(user);
		
		assertNotNull(user);
		assertNotNull(user.getId());
	}
	
	@Test
	public void createUserFailureCaseEmptyPasswords() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("");
		request.setPassword2("");
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(500, response.getStatusCodeValue());
		String error = (String) response.getBody();
		assertEquals(error, "Password cann't be empty");
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
	
	@Test
	public void addItemToUserCart() throws Exception{
		ModifyCartRequest request = new ModifyCartRequest("asaleh",1,1);
		ResponseEntity<Cart> response = cartController.addTocart(request);
		assertNotNull(response);
	}
	
	@Test
	public void removeItemToUserCart() throws Exception{
		ModifyCartRequest request = new ModifyCartRequest("asaleh",1,1);
		ResponseEntity<Cart> response = cartController.removeFromcart(request);
		assertNotNull(response);
	}
	
	
}
