package io.honeymon.springboot.totp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.honeymon.springboot.totp.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
