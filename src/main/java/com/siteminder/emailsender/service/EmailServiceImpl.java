package com.siteminder.emailsender.service;

import com.siteminder.emailsender.model.EmailRequest;
import com.siteminder.emailsender.model.EmailResponse;
import org.springframework.stereotype.Service;

import com.siteminder.emailsender.providers.EmailProvider;
import com.siteminder.emailsender.registry.EmailProviderRegistry;

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

        EmailResponse resp;
        try {
            resp = provider.send(emailRequest);
            if (resp.isSuccess()) {
                return resp;
            } else {
                for (EmailProvider fallback : providerRegistry.getAllProviders().values()) {
                    if (fallback != provider) {
                        return fallback.send(emailRequest);
                    }
                }
            }
        } catch (Exception ex) {
            // Attempt fallback with all other providers
            for (EmailProvider fallback : providerRegistry.getAllProviders().values()) {
                if (fallback != provider) {
                    try {
                        resp = fallback.send(emailRequest);
                        if (resp.isSuccess()) {
                            return resp;
                        }
                    } catch (Exception ignored) {
                        // Log and continue
                    }
                }
            }
            return new EmailResponse(false, "Failed to send email via all providers: " + ex.getMessage());
        }

        return resp;
    }

}
