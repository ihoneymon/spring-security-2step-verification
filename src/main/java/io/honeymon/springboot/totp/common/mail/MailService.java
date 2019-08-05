package io.honeymon.springboot.totp.common.mail;

import java.util.List;

public interface MailService {

	/**
	 * Send {@link MailMessage}
	 * 
	 * @param mailMessage
	 */
	void send(MailMessage mailMessage);
	
	/**
	 * Send {@link MailMessage} list
	 * @param mailMessages
	 */
	void send(List<MailMessage> mailMessages);
}
