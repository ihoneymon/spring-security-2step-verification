package io.honeymon.springboot.totp.core.user.application;

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

import io.honeymon.springboot.totp.core.user.domain.User;
import io.honeymon.springboot.totp.common.mail.MailMessage;
import io.honeymon.springboot.totp.common.mail.MailService;
import io.honeymon.springboot.totp.core.user.domain.UserRepository;
import lombok.Setter;

@Service
public class UserServiceImpl implements UserService {

	private static final String ISSUER = "honeymon";

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final IGoogleAuthenticator googleAuthenticator;
	private final MailService mailService;

	public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, IGoogleAuthenticator googleAuthenticator, MailService mailService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.googleAuthenticator = googleAuthenticator;
		this.mailService = mailService;
	}

	@Override
	public User register(String username, String password) {
		GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
		String encPassword = passwordEncoder.encode(password);

		User user = User.builder().username(username).password(encPassword).build();
		sendTOTPRegistrationMail(user, key);
		user.changeOtpSecretKey(key.getKey());
		
		return repository.save(user);
	}

	@Override
	public void delete(User user) {
		repository.delete(user);
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException(username + " is not found");
		}
		return user;
	}

}
