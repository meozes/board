package com.sparta.boards.controller;

import com.sparta.boards.dto.SignupRequestDto;
import com.sparta.boards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {

        return "board/login.html";
    }


    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
//        return "board/login.html";
        return "board/login.html";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "board/signup.html";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto, Model model) {
        try {
            userService.registerUser(requestDto);
        } catch (IllegalArgumentException e){
            // 에러 발생 시 이 쪽으로 이동되고, 에러메시지를 model 의 "error" 값으로 전달
            model.addAttribute("error", e.getMessage());
            return "board/signup.html";
        }
        return "redirect:/";
    }

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code) {
// authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);
        return "redirect:/";
    }
}
