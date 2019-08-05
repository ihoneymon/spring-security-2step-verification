package io.honeymon.springboot.totp.core.user.application;

import org.springframework.security.core.userdetails.UserDetailsService;

import io.honeymon.springboot.totp.core.user.domain.User;

public interface UserService extends UserDetailsService {

    /**
     * Register user
     *
     * @param username
     * @param password
     * @return
     */
    User register(String username, String password);


    /**
     * Delete User
     *
     * @param user
     */
    void delete(User user);
}
