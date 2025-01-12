package com.adam9e96.BlogStudy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 새 세션을 생성하지 않고 현재 세션만 확인
        if (session != null) {
            log.info("로그인 페이지 접속 - 세션 존재 - JSESSIONID: {}", session.getId());
        } else {
            log.info("로그인 페이지 접속 - 세션 없음");
        }

        return "login";
    }

    @GetMapping("/signup")
    public String signUp(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("회원가입 페이지 접속 - 세션 존재 - JSESSIONID: {}", session.getId());
        } else {
            log.info("회원가입 페이지 접속 - 세션 없음");
        }
        return "signup";
    }
}
