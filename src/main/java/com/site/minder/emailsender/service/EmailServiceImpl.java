package com.site.minder.emailsender.service;

import org.springframework.stereotype.Service;

import com.site.minder.emailsender.model.EmailRequest;
import com.site.minder.emailsender.model.EmailResponse;
import com.site.minder.emailsender.providers.EmailProvider;
import com.site.minder.emailsender.providers.MailGunEmailProvider;
import com.site.minder.emailsender.providers.SendGridEmailProvider;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailProvider mailgunProvider;
    private final EmailProvider sendGridProvider;

    public EmailServiceImpl(final MailGunEmailProvider mailgunProvider, final SendGridEmailProvider sendGridProvider) {
        this.mailgunProvider = mailgunProvider;
        this.sendGridProvider = sendGridProvider;
    }

	@Override
	public EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
        	//return sendGridProvider.send(emailRequest);
            return mailgunProvider.send(emailRequest);
        } catch (Exception ex) {
            //return sendGridProvider.send(emailRequest); // Fallback to SendGrid
        	return new EmailResponse(false, "Failed to send email via all providers: " + ex.getMessage());
        }
	}
}
