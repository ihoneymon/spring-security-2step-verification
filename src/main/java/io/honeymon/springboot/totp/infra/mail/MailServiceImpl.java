package io.honeymon.springboot.totp.infra.mail;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import lombok.Setter;

@Component
@EnableConfigurationProperties(MailProperties.class)
public class MailServiceImpl implements MailService {

	@Autowired
	MailProperties mailProperties;
	
	@Setter
	@Autowired
	JavaMailSender javaMailSender;
	
	@Setter
	@Autowired
	SpringTemplateEngine emailTemplateEngine;
	

	@Override
	public void send(MailMessage mailMessage) {
		doSend(mailMessage);
	}

	@Override
	public void send(List<MailMessage> mailMessages) {
		mailMessages.stream().forEach(mailMessage -> send(mailMessage));
		
	}
	
	private void doSend(MailMessage mailMessage) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, mailMessage.isMultipart(), "UTF-8");
			helper.setFrom(mailProperties.getUsername());
			helper.setTo(mailMessage.getTo());
			helper.setSubject(mailMessage.getSubject());
			if(mailMessage.hasTemplateName()) {
				IContext context = new Context(LocaleContextHolder.getLocale(), mailMessage.getAttributes());
				mailMessage.setText(emailTemplateEngine.process(mailMessage.getTemplateName(), context));
				mailMessage.setHtml(true);
			}
			helper.setText(mailMessage.getText(), mailMessage.isHtml());
			
			if (mailMessage.getReplyTo() != null) {
                helper.setReplyTo(mailMessage.getReplyTo());
            }
            if (mailMessage.getCc() != null) {
                helper.setCc(mailMessage.getCc());
            }
            if (mailMessage.getBcc() != null) {
                helper.setBcc(mailMessage.getBcc());
            }
            if (mailMessage.getAttachments() != null) {
                for (File attachment : mailMessage.getAttachments()) {
                    helper.addAttachment(attachment.getName(), attachment);
                }
            }

            javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			// TODO Auto-generated catch 
		}
		
	}
	
}
