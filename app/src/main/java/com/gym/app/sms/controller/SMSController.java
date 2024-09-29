package com.gym.app.sms.controller;

import com.gym.app.dto.OtpRequest;
import com.gym.app.dto.OtpResponseDto;
import com.gym.app.dto.OtpValidationRequest;
import com.gym.app.sms.SMSRequest;
import com.gym.app.sms.service.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@Slf4j
public class SMSController {

    @Autowired
    private SMSService smsService;

    @GetMapping("/process")
    public String processSMS() {
        return "SMS sent";
    }

    @PostMapping("/send-otp")
    public OtpResponseDto sendOtp(@RequestBody SMSRequest smsRequest) {
        log.info("inside sendOtp :: "+smsRequest.getDestinationSMSNumber());
        return smsService.sendSMS(smsRequest);
    }
    @PostMapping("/validate-otp")
    public String validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        log.info("inside validateOtp :: "+otpValidationRequest.getUsername()+" "+otpValidationRequest.getOtpNumber());
        return smsService.validateOtp(otpValidationRequest);
    }

}