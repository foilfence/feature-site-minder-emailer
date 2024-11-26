package com.siteminder.emailsender.providers;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.siteminder.emailsender.model.EmailRequest;
import com.siteminder.emailsender.model.EmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.siteminder.emailsender.registry.EmailProviderRegistry;

import jakarta.annotation.PostConstruct;

@Component
public class SendGridEmailProvider implements EmailProvider {
	
	 private static final Logger logger = LoggerFactory.getLogger(SendGridEmailProvider.class);
	
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;
    
    @Autowired
    private final EmailProviderRegistry emailProviderRegistry;
    
	public SendGridEmailProvider(final EmailProviderRegistry emailProviderRegistry) {
		this.emailProviderRegistry = emailProviderRegistry;
	}
	
    @PostConstruct
    public void init() {
    	emailProviderRegistry.registerProvider("sendgrid", this);
    }


	@Override
	public EmailResponse send(final EmailRequest emailRequest) {
		
		final Email from = new Email(fromEmail);
	    final String subject = emailRequest.getSubject();
	    final String body = emailRequest.getBody();
	    
        // Create content
        final Content content = new Content("text/plain", body);
        
        // Create mail and personalization object
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        final Personalization personalization = new Personalization();
	    
        // Add "To" recipients
        addRecipients(personalization::addTo, emailRequest.getTo());

        // Add "Cc" recipients
        addRecipients(personalization::addCc, emailRequest.getCc());

        // Add "Bcc" recipients
        addRecipients(personalization::addBcc, emailRequest.getBcc());

        mail.addPersonalization(personalization);

        // Send email using SendGrid API
        final SendGrid sg = new SendGrid(sendGridApiKey);
        final Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            final Response response = sg.api(request);
            
            System.out.println(response.getStatusCode());
            
            if (response.getStatusCode() == 202) {
                logger.info("Email sent successfully. Status: {}, Body: {}, Headers: {}",
                        response.getStatusCode(), response.getBody(), response.getHeaders());
                return new EmailResponse(true, "Email sent successfully via SendGrid.");
            } else {
                logger.error("Failed to send email. Status: {}, Body: {}, Headers: {}",
                        response.getStatusCode(), response.getBody(), response.getHeaders());
                return new EmailResponse(false, "Failed to send email via SendGrid. Status: " + response.getStatusCode());
            }
            
        } catch (IOException ex) {
            logger.error("Error sending email: {}", ex.getMessage(), ex);
            return new EmailResponse(false, "Failed to send email: " + ex.getMessage());
        }
        
	}

	/**
     * Utility method to add recipients to the given personalization.
     *
     * @param addMethod  the method reference to add recipients (e.g., addTo, addCc, addBcc)
     * @param recipients the list of recipient emails
     */
    private void addRecipients(Consumer<Email> addMethod, List<String> recipients) {
        if (recipients != null) {
            recipients.stream()
                    .map(Email::new)
                    .forEach(addMethod);
        }
    }
	
}
