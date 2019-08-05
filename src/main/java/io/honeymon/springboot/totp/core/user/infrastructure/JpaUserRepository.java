package io.honeymon.springboot.totp.core.user.infrastructure;

import io.honeymon.springboot.totp.core.user.domain.User;
import io.honeymon.springboot.totp.core.user.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}
