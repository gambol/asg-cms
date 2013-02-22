/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class ImageValidationController {

    @RequestMapping(value = "validateImage")
    public @ResponseBody boolean validateCaptchaImage(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Boolean isResponseCorrect = Boolean.TRUE;
        //remenber that we need an id to validate!       

        String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String kaptchaReceived = request.getParameter("captcha");
        if (kaptchaReceived == null || !kaptchaReceived.equalsIgnoreCase(kaptchaExpected)) {
            isResponseCorrect = false;
        } 
        
        /*
        // httpServletResponse.encodeUrl("sucess.html");    
        if (isResponseCorrect.booleanValue()) {
            response.sendRedirect("success.html");
        } else {
            response.sendRedirect("failture.html");
        }
        */
        
       // return String.valueOf(isResponseCorrect);
        return isResponseCorrect;
        
    }
}
