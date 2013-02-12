/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author xiang.fu
 */
@Controller
@RequestMapping("/test/")
public class TestController {
    
    @RequestMapping("1.htm")
    public @ResponseBody Test getT(){
        Test t = new Test();
        t.setA("abc");
        t.setB("def");
        return t;
    }
    
    
}
