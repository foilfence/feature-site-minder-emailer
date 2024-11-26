package com.siteminder.emailsender.service;

import com.siteminder.emailsender.model.EmailRequest;
import com.siteminder.emailsender.model.EmailResponse;

public interface EmailService {
	EmailResponse sendEmail(final EmailRequest emailRequest);
}
