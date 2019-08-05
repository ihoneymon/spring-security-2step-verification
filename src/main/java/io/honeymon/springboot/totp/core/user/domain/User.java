package io.honeymon.springboot.totp.core.user.domain;

import io.honeymon.springboot.totp.security.RoleAuthority;
import io.honeymon.springboot.totp.security.TOTPUserDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class User implements TOTPUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String password;

    @Column
    private String otpSecretKey;    // OTP 비밀키

    @ElementCollection(fetch = FetchType.EAGER, targetClass = RoleAuthority.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_authorities")
    private Set<RoleAuthority> authorities = new HashSet<>();    // 스프링시큐리티에서 권한은 EAGER 여야한다.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getSecretKey() {
        return this.otpSecretKey;
    }

    public User changePassword(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입력값입니다.");
        }

        this.password = password;
        return this;
    }

    public User changeOtpSecretKey(String otpSecretKey) {
        if (Objects.isNull(otpSecretKey) || otpSecretKey.isEmpty()) {
            throw new IllegalArgumentException("OTP 비밀키는 필수입력값입니다.");
        }

        this.otpSecretKey = otpSecretKey;
        return this;
    }
}
