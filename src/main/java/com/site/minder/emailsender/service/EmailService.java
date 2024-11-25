package com.site.minder.emailsender.service;

import com.site.minder.emailsender.model.EmailRequest;
import com.site.minder.emailsender.model.EmailResponse;

public interface EmailService {
	EmailResponse sendEmail(final EmailRequest emailRequest);
}
