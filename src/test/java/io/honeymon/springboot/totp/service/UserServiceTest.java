package io.honeymon.springboot.totp.service;


import io.honeymon.springboot.totp.core.user.application.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.honeymon.springboot.totp.core.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void register() {
		String username = "ihoneymon@gmail.com"; // 테스트 할떄 변경하세요.
		String rawPassword ="1234";
		User user = userService.register(username, rawPassword);
		
		assertThat(passwordEncoder.matches(rawPassword, user.getPassword())).isTrue();
	}

}
