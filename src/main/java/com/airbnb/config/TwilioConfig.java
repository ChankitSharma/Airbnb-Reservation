package com.airbnb.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    //Spring will manage the initialization of beans defined in configuration classes automatically.
    @Bean
    public void twilioInit() {
        Twilio.init(accountSid, authToken);
    }
}
