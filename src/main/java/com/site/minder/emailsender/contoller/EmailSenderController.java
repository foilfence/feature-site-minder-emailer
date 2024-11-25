package com.site.minder.emailsender.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.site.minder.emailsender.model.EmailRequest;
import com.site.minder.emailsender.model.EmailResponse;
import com.site.minder.emailsender.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/emails")
public class EmailSenderController {
    private final EmailService emailService;

    public EmailSenderController(final EmailService emailService) {
        this.emailService = emailService;
    }
    
    @GetMapping("/health")
    public String healthCheck() {
        return "Application is running!";
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
    	final EmailResponse response = emailService.sendEmail(emailRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(500).body(response.getMessage());
        }
    }
}
