package io.honeymon.springboot.totp.domain;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import io.honeymon.springboot.totp.security.RoleAuthority;
import io.honeymon.springboot.totp.security.TOTPUserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements TOTPUserDetails {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=100)
	private String username;
	
	@Column(length=100)
	private String password;
	
	@Column
	private String otpSecretKey;	// OTP 비밀
	
	@Transient
	private String plainPassword;
	
	@ElementCollection(fetch=FetchType.EAGER, targetClass=RoleAuthority.class)
	@Enumerated(EnumType.STRING)
    @CollectionTable(name="user_authorities")
	Set<RoleAuthority> authorities;	// 스프링시큐리티에서 권한은 EAGER 여야한다.
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
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

}
