package io.honeymon.springboot.totp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import io.honeymon.springboot.totp.SpringSecurity2stepVerificationApplication;
import io.honeymon.springboot.totp.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SpringSecurity2stepVerificationApplication.class)
@WebAppConfiguration
public class UserServiceTest {

	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void addUser() {
		String username = "ihoneymon@gmail.com"; // 테스트 할떄 변경하세요.
		String rawPassword ="1234";
		User user = userService.insert(User.builder().username(username).plainPassword(rawPassword).build());
		
		assertThat(passwordEncoder.matches(rawPassword, user.getPassword()), is(true));
	}

}
