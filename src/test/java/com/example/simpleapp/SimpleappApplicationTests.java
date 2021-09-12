package com.example.simpleapp;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.simpleapp.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleappApplicationTests {
	@Autowired
	private UserController controller;
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
