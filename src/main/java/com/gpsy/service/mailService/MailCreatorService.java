package com.gpsy.service.mailService;

import com.gpsy.config.MailMessageConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailCreatorService {

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildPlaylistCompositionEmail(String message) {
        Context context = new Context();
        context.setVariable("welcomeMessage", MailMessageConfiguration.WELCOME_MESSAHE);
        context.setVariable("message", message);
        context.setVariable("goodByeMessage", MailMessageConfiguration.GOODBYE_MESSAGE);
        context.setVariable("companyName", MailMessageConfiguration.COMPANY_NAME);
        return templateEngine.process("mail/new-composition-of-playlist-mail", context);
    }
}
