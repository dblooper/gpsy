package com.gpsy.service.mailService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTestSuite {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    MailCreatorService mailCreatorService;

    @Test
    public void shouldSendEmail() {
        //Given
        Mail mail = new Mail("test@daniel.pl", "Test_subject", "Test_message");

        //When
        emailService.send(mail);
        //Then
        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }
}