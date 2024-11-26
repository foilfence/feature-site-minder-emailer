package com.site.minder.emailsender.registry;

import org.springframework.stereotype.Component;

import com.site.minder.emailsender.providers.EmailProvider;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailProviderRegistry {
    private final Map<String, EmailProvider> providers = new HashMap<>();

    public void registerProvider(final String name, final EmailProvider provider) {
        providers.put(name.toLowerCase(), provider);
    }

    public EmailProvider getProvider(final String name) {
        return providers.get(name.toLowerCase());
    }

    public EmailProvider getDefaultProvider() {
        return providers.getOrDefault("mailgun", null);
    }

    public Map<String, EmailProvider> getAllProviders() {
        return providers;
    }
}

