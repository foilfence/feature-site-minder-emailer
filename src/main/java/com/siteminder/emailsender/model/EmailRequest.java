package com.siteminder.emailsender.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EmailRequest {
    @NotEmpty(message = "Sender email is required")
    @Email(message = "Invalid sender email")
    private String from;

    @NotEmpty(message = "Recipient email is required")
    private List<@Email(message = "Invalid recipient email") String> to;

    private List<@Email(message = "Invalid CC email") String> cc;
    private List<@Email(message = "Invalid BCC email") String> bcc;

    @NotEmpty(message = "Subject is required")
    private String subject;

    @NotEmpty(message = "Body is required")
    private String body;
    
    // Optional provider type, no validation since it's optional
    private String providerType;
}
