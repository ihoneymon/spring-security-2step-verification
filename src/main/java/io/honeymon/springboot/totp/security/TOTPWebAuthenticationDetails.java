package io.honeymon.springboot.totp.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import lombok.Getter;

/**
 * 로그인폼에 있는 "totp-verification-code" 정보를 읽어온다.
 * @author honeymon
 *
 */
@Getter
public class TOTPWebAuthenticationDetails extends WebAuthenticationDetails {
	public static final String TOTP_VERIFICATION_CODE = "totp-verification-code";
	private Integer totpKey;
	
	/**
     * Records the remote address and will also set the session Id if a session
     * already exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
	public TOTPWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
        String totpKeyString = request.getParameter(TOTP_VERIFICATION_CODE);
        if (StringUtils.hasText(totpKeyString)) {
            try {
                this.totpKey = Integer.valueOf(totpKeyString);
            } catch (NumberFormatException e) {
                this.totpKey = null;
            }
        }
	}

}
