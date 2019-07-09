package com.gpsy.service.mailService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Mail {

    private String sendTo;
    private String Subject;
    private String message;
}
