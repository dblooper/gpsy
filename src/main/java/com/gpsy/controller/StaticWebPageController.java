package com.gpsy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticWebPageController {

    @RequestMapping("/")
    public String index() {
        return  "mail/new-composition-of-playlist-mail";
    }
}
