package com.siteminder.emailsender.service;

import com.siteminder.emailsender.model.EmailRequest;
import com.siteminder.emailsender.providers.EmailProvider;
import com.siteminder.emailsender.providers.MailGunEmailProvider;
import com.siteminder.emailsender.providers.SendGridEmailProvider;
import com.siteminder.emailsender.registry.EmailProviderRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailServiceImplTest {

    EmailProviderRegistry registry = mock(EmailProviderRegistry.class);

    EmailService svc = new EmailServiceImpl(registry);

    @Test
    void testSendEmailViaSendGrind_fallbackToMailgunError() {
        EmailRequest req = mock(EmailRequest.class);
        EmailProvider sg = new SendGridEmailProvider(registry);
        EmailProvider mg = new MailGunEmailProvider(null, registry);

        Map<String, EmailProvider> map = new HashMap<>();
        map.put("sendgrid", sg);
        map.put("mailgun", mg);

        when(registry.getDefaultProvider()).thenReturn(sg);
        when(registry.getAllProviders()).thenReturn(map);

        String msg = svc.sendEmail(req).getMessage();
        assertTrue(msg.contains("\"this.restTemplate\" is null"));
    }

    @Test
    void testSendEmailViaMailGun_fallbackToSendGridError() {
        EmailRequest req = mock(EmailRequest.class);
        EmailProvider sg = new SendGridEmailProvider(registry);
        RestTemplate template = mock(RestTemplate.class);
        EmailProvider mg = new MailGunEmailProvider(template, registry);

        Map<String, EmailProvider> map = new HashMap<>();
        map.put("sendgrid", sg);
        map.put("mailgun", mg);

        when(registry.getDefaultProvider()).thenReturn(mg);
        when(registry.getAllProviders()).thenReturn(map);

        String msg = svc.sendEmail(req).getMessage();
        System.out.println(msg);
        assertTrue(msg.contains("all providers"));
    }

}
