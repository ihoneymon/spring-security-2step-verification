package io.honeymon.springboot.totp.core.user.domain;

public interface UserFinder {
    User findByUsername(String username);
    User findById(Long id);
}
