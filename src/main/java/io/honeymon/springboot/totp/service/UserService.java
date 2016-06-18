package io.honeymon.springboot.totp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import io.honeymon.springboot.totp.domain.User;

public interface UserService extends UserDetailsService {

	/**
	 * Save new User
	 * @param user
	 * @return
	 */
	User insert(User user);
	
	/**
	 * Update User
	 * @param user
	 * @return
	 */
	User update(User user);
	
	/**
	 * Delete User
	 * @param user
	 */
	void delete(User user);
}
