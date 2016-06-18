package io.honeymon.springboot.totp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

import io.honeymon.springboot.totp.domain.User;
import io.honeymon.springboot.totp.infra.mail.MailMessage;
import io.honeymon.springboot.totp.infra.mail.MailService;
import io.honeymon.springboot.totp.repository.UserRepository;
import lombok.Setter;

@Service
public class UserServiceImpl implements UserService {

	private static final String ISSUER = "honeymon";

	@Setter
	@Autowired
	UserRepository repository;
	
	@Setter
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Setter
	@Autowired
	IGoogleAuthenticator googleAuthenticator;
	
	@Setter
	@Autowired
	MailService mailService;
	
	@Override
	public User insert(User user) {
		GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
		sendTOTPRegistrationMail(user, key);
		user.setOtpSecretKey(key.getKey());
		
		encodePassword(user);
		return repository.save(user);
	}

	private void sendTOTPRegistrationMail(User user, GoogleAuthenticatorKey key) {
		String qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(ISSUER, user.getUsername(), key);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("qrCodeUrl", qrCodeUrl);
		
		MailMessage mailMessage = MailMessage.builder()
				.templateName(MailMessage.OTP_REGISTRATION)
				.to(new String[]{user.getUsername()})
				.subject("Honeymon TOTP Registration mail")
				.attributes(attributes)
				.build();
		mailService.send(mailMessage);
	}

	private void encodePassword(User user) {
		if(StringUtils.hasText(user.getPlainPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPlainPassword()));
		}
	}

	@Override
	public User update(User user) {
		encodePassword(user);
		return repository.save(user);
	}

	@Override
	public void delete(User user) {
		repository.delete(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException(username + " is not found");
		}
		return user;
	}

}
