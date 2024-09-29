package com.gym.app;import com.gym.app.dto.OtpRequest;
import com.gym.app.dto.OtpResponseDto;
import com.gym.app.dto.OtpValidationRequest;
import com.gym.app.enums.OtpStatus;
import com.gym.app.security.config.TwilioConfig;
import com.gym.app.sms.service.SMSService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SMSServiceTest {

    @Mock
    private TwilioConfig twilioConfig;

    @InjectMocks
    private SMSService smsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(twilioConfig.getAccountSid()).thenReturn("ACd2c8b8550b126377cefbcaf4f2cd8c20");
        when(twilioConfig.getAuthToken()).thenReturn("87eeb501ff18842733861c62f5495611");
        when(twilioConfig.getTrialNumber()).thenReturn("+12084358829");
    }

    @Test
    public void testSendSMS() {
        // Given
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setPhoneNumber("+306947277137");
        otpRequest.setUsername("testUser");

        // Mocking the MessageCreator instance
        MessageCreator mockMessageCreator = mock(MessageCreator.class);
        Message mockMessage = mock(Message.class);
        when(mockMessage.getSid()).thenReturn("SM1234567890");
        when(mockMessageCreator.create()).thenReturn(mockMessage);

        // Mocking the static Message.creator method
        try (var mockedMessage = mockStatic(Message.class)) {
            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), any(String.class)))
                    .thenReturn(mockMessageCreator);

            // When
            OtpResponseDto response = smsService.sendSMS(otpRequest);

            // Then
            verify(mockMessageCreator).create();
            assertEquals(OtpStatus.DELIVERED, response.getStatus());
            assertTrue(response.getMessage().contains("Your OTP is"));
        }
    }
}