package com.tt.common.mail;

import java.io.CharArrayWriter;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerMailSender implements RichMailSender {

	private static final Logger logger = LoggerFactory.getLogger(FreeMarkerMailSender.class);

	private Configuration configuration;

	private JavaMailSender mailSender;

	public FreeMarkerMailSender(Configuration configuration, JavaMailSender mailSender) {
		this.configuration = configuration;
		this.mailSender = mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void send(final Object templateId, final SimpleMailMessage msg, final Map<String, Object> variables) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(msg.getTo());
				message.setFrom(msg.getFrom());
				message.setSubject(msg.getSubject());
				Template template = configuration.getTemplate(templateId.toString());
				CharArrayWriter out = new CharArrayWriter();
				template.process(variables, out);
				String body = out.toString();
				logger.debug("body={}", body);
				message.setText(body, true);
			}
		};

		mailSender.send(preparator);
		logger.debug("sent message with template id={}", templateId);
	}

}
