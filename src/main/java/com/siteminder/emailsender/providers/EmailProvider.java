package com.siteminder.emailsender.providers;

import com.siteminder.emailsender.model.EmailRequest;
import com.siteminder.emailsender.model.EmailResponse;

public interface EmailProvider {
	EmailResponse send(final EmailRequest emailRequest);
}