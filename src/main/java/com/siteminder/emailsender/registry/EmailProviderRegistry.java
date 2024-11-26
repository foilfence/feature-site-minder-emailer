package com.siteminder.emailsender.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.siteminder.emailsender.providers.EmailProvider;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailProviderRegistry {
    private final Map<String, EmailProvider> providers = new HashMap<>();

    @Value("${mail.default.provider}")
    private String defaultProvider;

    public void registerProvider(final String name, final EmailProvider provider) {
        providers.put(name.toLowerCase(), provider);
    }

    public EmailProvider getProvider(final String name) {
        return providers.get(name.toLowerCase());
    }

    public EmailProvider getDefaultProvider() {
        return providers.getOrDefault(defaultProvider, null);
    }

    public Map<String, EmailProvider> getAllProviders() {
        return providers;
    }
}

