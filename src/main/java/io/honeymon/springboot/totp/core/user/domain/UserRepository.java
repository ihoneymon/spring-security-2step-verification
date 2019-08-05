package io.honeymon.springboot.totp.core.user.domain;

import java.util.Optional;

public interface UserRepository {

	User findByUsername(String username);

	User save(User user);

	Optional<User> findById(Long id);

	void delete(User user);
}
