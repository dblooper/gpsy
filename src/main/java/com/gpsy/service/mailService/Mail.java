package com.gpsy.service.mailService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Mail {

    private String sendTo;
    private String subject;
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mail mail = (Mail) o;

        if (!sendTo.equals(mail.sendTo)) return false;
        if (!subject.equals(mail.subject)) return false;
        return message.equals(mail.message);
    }

    @Override
    public int hashCode() {
        int result = sendTo.hashCode();
        result = 31 * result + subject.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
