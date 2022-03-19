package com.example.springbootkakaoexample;

import com.example.springbootkakaoexample.domain.account.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/account/profile")
    public String profile(Model model) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("nickname", account.getNickname());
        model.addAttribute("profileUrl", account.getProfileUrl());
        return "auth/profile";
    }

}
