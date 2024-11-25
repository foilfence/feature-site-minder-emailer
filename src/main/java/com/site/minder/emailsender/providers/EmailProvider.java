package com.site.minder.emailsender.providers;

import com.site.minder.emailsender.model.EmailRequest;
import com.site.minder.emailsender.model.EmailResponse;

public interface EmailProvider {
	EmailResponse send(final EmailRequest emailRequest);
}