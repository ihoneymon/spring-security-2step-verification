package io.honeymon.springboot.totp.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void ecode() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "1234";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
		System.out.println(">> " + encodedPassword);
		
	}
	
}
