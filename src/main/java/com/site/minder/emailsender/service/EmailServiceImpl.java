package com.site.minder.emailsender.service;

import org.springframework.stereotype.Service;

import com.site.minder.emailsender.model.EmailRequest;
import com.site.minder.emailsender.model.EmailResponse;
import com.site.minder.emailsender.providers.EmailProvider;
import com.site.minder.emailsender.providers.MailGunEmailProvider;
import com.site.minder.emailsender.providers.SendGridEmailProvider;
import com.site.minder.emailsender.registry.EmailProviderRegistry;

@Service
public class EmailServiceImpl implements EmailService {

	private final EmailProviderRegistry providerRegistry;

    public EmailServiceImpl(final EmailProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    @Override
    public EmailResponse sendEmail(final EmailRequest emailRequest) {
        final String providerType = emailRequest.getProviderType();
        
        EmailProvider provider = null;
        if (providerType != null) {
        	provider = providerRegistry.getProvider(providerType);
        }
        
        if (provider == null) {
            provider = providerRegistry.getDefaultProvider();
        }

        if (provider == null) {
            return new EmailResponse(false, "No email provider available.");
        }

        try {
            return provider.send(emailRequest);
        } catch (Exception ex) {
            // Attempt fallback with all other providers
            for (EmailProvider fallback : providerRegistry.getAllProviders().values()) {
                if (fallback != provider) {
                    try {
                        return fallback.send(emailRequest);
                    } catch (Exception ignored) {
                        // Log and continue
                    }
                }
            }
            return new EmailResponse(false, "Failed to send email via all providers: " + ex.getMessage());
        }
    }
}
