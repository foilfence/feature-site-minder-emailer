package com.siteminder.emailsender.providers;

import java.util.Base64;
import java.util.List;

import com.siteminder.emailsender.model.EmailRequest;
import com.siteminder.emailsender.model.EmailResponse;
import com.siteminder.emailsender.registry.EmailProviderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MailGunEmailProvider implements EmailProvider {

    private static final Logger logger = LoggerFactory.getLogger(MailGunEmailProvider.class);

    @Value("${mailgun.api.url}")
    private String mailgunApiUrl;

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.from.email}")
    private String fromEmail;

    @Autowired
    private final RestTemplate restTemplate;
    
    @Autowired
    private final EmailProviderRegistry emailProviderRegistry;

    public MailGunEmailProvider(final RestTemplate restTemplate, final EmailProviderRegistry emailProviderRegistry) {
        this.restTemplate = restTemplate;
		this.emailProviderRegistry = new EmailProviderRegistry();
    }
    
    @PostConstruct
    public void init() {
    	emailProviderRegistry.registerProvider("mailgun", this);
    }

    @Override
    public EmailResponse send(final EmailRequest emailRequest) {
        final String url = mailgunApiUrl + "/messages";

        // Prepare the request body
        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("from", fromEmail);
       
        addRecipients(requestBody, "to", emailRequest.getTo());
        addRecipients(requestBody, "cc", emailRequest.getCc());
        addRecipients(requestBody, "bcc", emailRequest.getBcc());

        requestBody.add("subject", emailRequest.getSubject());
        requestBody.add("text", emailRequest.getBody());

        // Set the headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final String authHeader = "Basic " + Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes());
        headers.set("Authorization", authHeader);

        // Create the HTTP entity
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Send the request
        final ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Email sent successfully.");
            return new EmailResponse(true, "Email sent successfully");
        } else {
            logger.error("Failed to send email. Status: {}, Body: {}",
                    response.getStatusCodeValue(),
                    response.getBody());
            return new EmailResponse(false, "Failed to send email: " + response.getBody());
        }
    }
    
	/**
	 * Adds a list of recipients to the specified key in the request body.
	 *
	 * @param requestBody the request body to update
	 * @param key         the recipient type (e.g., "to", "cc", "bcc")
	 * @param recipients  the email addresses to add
	 */
    private void addRecipients(final MultiValueMap<String, String> requestBody, final String key, List<String> recipients) {
        if (recipients != null && !recipients.isEmpty()) {
            requestBody.add(key, String.join(",", recipients));
        }
    }

}
