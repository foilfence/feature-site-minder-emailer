package com.siteminder.emailsender.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
	private boolean success;
    private String message;
}
