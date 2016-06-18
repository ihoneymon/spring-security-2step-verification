package io.honeymon.springboot.totp.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.warrenstrange.googleauth.IGoogleAuthenticator;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtensibleUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Setter
    private PasswordEncoder passwordEncoder;
    @Setter
    private UserDetailsService userDetailsService;
    @Setter
    private UserDetailsChecker loginPostUserDetailsChecker;
    @Setter
    private IGoogleAuthenticator authenticator;
    
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		log.info("additionalAuthenticationChecks: username={}", userDetails.getUsername());
		if (authentication.getCredentials() == null) {
            log.error("Authentication failed: no credentials provided.");
            throw new BadCredentialsException("No Credentials");
        }

        String credentialsPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(credentialsPassword, userDetails.getPassword())) {
            log.error("Authentication failed: password does not match stored value.");
            throw new BadCredentialsException("BadCredentials");
        }
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		log.info("retrieveUser: {}", username);
        TOTPUserDetails retrieveUser;

        try {
            retrieveUser = (TOTPUserDetails) userDetailsService.loadUserByUsername(username);
            
            if (retrieveUser == null) {
                throw new InternalAuthenticationServiceException("UserDetails returned null.");
            }
            
            if(!StringUtils.hasText(retrieveUser.getSecretKey())) {
            	throw new BadCredentialsException("User don't registry TOTP.");
            }
            
            Integer verificationCode = ((TOTPWebAuthenticationDetails) authentication.getDetails()).getTotpKey();
            if (verificationCode != null) {
            	if (!authenticator.authorize(retrieveUser.getSecretKey(), verificationCode)) {
            		throw new BadCredentialsException("Invalid VerificationCode");
            	}
            } else {
            	throw new BadCredentialsException("TOTP is Mandatory");
            }
        } catch (UsernameNotFoundException notFoundException) {
            if (hideUserNotFoundExceptions) {
                throw new BadCredentialsException("Bad Credentials.");
            }
            throw notFoundException;
        } catch (Exception authenticationProblem) {
            throw new InternalAuthenticationServiceException(authenticationProblem.getMessage(), authenticationProblem);
        }

        return retrieveUser;
	}

}
