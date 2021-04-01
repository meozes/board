package com.sparta.boards.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//@Slf4j
//@ControllerAdvice
//public class MemberController {
//    @ExceptionHandler(value = IllegalArgumentException.class)
//    //public ModelAndView handleDemoExceptionForGlobal(IllegalArgumentException e){
//    public String handleDemoExceptionForGlobal(IllegalArgumentException e){
//        log.error(e.getMessage());
//        return "redirect:/";
////        return new ModelAndView("error/throwExceptionMessage").addObject("errorMessage",e.getMessage());
//    }
//}
