package io.honeymon.springboot.totp.infra.mail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailMessage {
	public final static String OTP_REGISTRATION = "otp-registration";

	private String from;

	private String replyTo;

	private String[] to;

	private String[] cc;

	private String[] bcc;

	private Date sentDate;

	private String subject;

	private String text;
	
	private boolean html;
	
	private List<File> attachments;
	
	private String templateName;
	
	private Map<String, Object> attributes;

	public boolean isMultipart() {
		return CollectionUtils.isEmpty(this.attachments);
	}

	public boolean hasTemplateName() {
		return StringUtils.hasText(this.templateName);
	}
}
