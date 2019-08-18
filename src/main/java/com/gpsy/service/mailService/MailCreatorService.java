package com.gpsy.service.mailService;

import com.gpsy.config.ConstantsForConfiguration;
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
        context.setVariable("welcomeMessage", ConstantsForConfiguration.WELCOME_MESSAHE);
        context.setVariable("message", message);
        context.setVariable("goodByeMessage", ConstantsForConfiguration.GOODBYE_MESSAGE);
        context.setVariable("companyName", ConstantsForConfiguration.COMPANY_NAME);
        context.setVariable("buttonLink", ConstantsForConfiguration.ANCHOR_BUTTON_LINK);
        return templateEngine.process("mail/new-composition-of-playlist-mail", context);
    }
}
