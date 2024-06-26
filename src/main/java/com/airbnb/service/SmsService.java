package com.airbnb.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public void sendSms(String toPhoneNumber, String messageBody) {
//        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                messageBody
        ).create();
        System.out.println("SMS sent with SID: " + message.getSid());
    }
}
