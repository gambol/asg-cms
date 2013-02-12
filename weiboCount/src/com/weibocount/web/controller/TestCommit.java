/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiang.fu
 */
@Controller
@RequestMapping("/testcommit/")
public class TestCommit {
    
    @RequestMapping(value="c.htm")
    public void commit(@RequestBody Test t){
        System.out.println(t.getA());
        System.out.println(t.getB());
        System.out.println("fff");
    }
    
    
}
