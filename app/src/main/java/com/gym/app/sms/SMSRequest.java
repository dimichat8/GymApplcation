package com.gym.app.sms;

import lombok.Data;

@Data
public class SMSRequest {
    private String destinationSMSNumber;
    private String smsMessage;

}
