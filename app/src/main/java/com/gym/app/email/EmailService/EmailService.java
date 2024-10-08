package com.gym.app.email.EmailService;

import com.gym.app.dto.EmailDto;
import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<EmailDto> sendEmail(EmailDto emailDto);
}
