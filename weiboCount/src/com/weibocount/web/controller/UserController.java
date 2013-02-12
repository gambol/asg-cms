/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.weibocount.model.User;
import com.weibocount.utils.DateUtil;
import com.weibocount.web.common.EmailSenderDriver;
import com.weibocount.web.common.SessionConst;
import com.weibocount.web.common.UserBean;
import com.weibocount.web.dao.UserDao;

/**
 *
 *
 * @author xiang.fu
 */
@Controller
@RequestMapping("/user")
public class UserController {

    // Invoked initially to create the "form" attribute
    // Once created the "form" attribute comes from the HTTP session (see @SessionAttributes)
    @ModelAttribute("userBean")
    public UserBean createFormBean() {
        return new UserBean();
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String form(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {

        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@Valid UserBean loginBean, BindingResult result, HttpServletRequest request, HttpServletResponse response,
            Model model) {

        if (result.hasErrors()) {

            System.err.println(result.toString() + "   " + (result.getAllErrors()).get(0).toString());
            return null;
        }

        String userEmail = loginBean.getUserEmail();
        String password = loginBean.getPassword();

        User user = new UserDao().getUserByEmail(userEmail);
        if (user == null) {
            model.addAttribute("mailErrMsg", "用户不存在");
            return "login";
        } else if (user.getUsername().equalsIgnoreCase(userEmail) && user.getPassword().equalsIgnoreCase(password)) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.USERID, user.getId());
            session.setAttribute(SessionConst.USERNAME, user.getUsername());
            return "redirect:/index.htm";
        } else {
            model.addAttribute("mailErrMsg", "用户名密码不正确");
            return "login";
        }
    }
    
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConst.AT);
        return "index";
    }
    

    @RequestMapping(value = "regi", method = RequestMethod.GET)
    public String regiform(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {

        return "regi";
    }

    @RequestMapping(value = "regi", method = RequestMethod.POST)
    public String regi(@Valid UserBean loginBean, BindingResult result, HttpServletRequest request, HttpServletResponse response,
            Model model) {

        if (result.hasErrors()) {

            System.err.println(result.toString() + "   " + (result.getAllErrors()).get(0).toString());
            return null;
        }
        String tos = loginBean.getTos();
        if(!"yes".equals(tos)){
            model.addAttribute("tosErrMsg", "要使用我们的服务，您必须同意接受 Atugame 的用户协议。");
            return "regi";
        }
        
        String userEmail = loginBean.getUserEmail();
        String password = loginBean.getPassword();
        UserDao userDao = new UserDao();
        User user = userDao.getUserByEmail(userEmail);
        if (user != null) {
            model.addAttribute("mailErrMsg", "用户已存在");
            return "regi";
        }
        user = new User();
        user.setUsername(userEmail);
        user.setPassword(password);
        user.setEmail(userEmail);
        user.setCreateDate(DateUtil.getCurrentTimestamp());

        userDao.addUser(user);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.USERID, user.getId());
        session.setAttribute(SessionConst.USERNAME, user.getUsername());
        return "redirect:/index.htm";
    }

    @RequestMapping(value = "forget", method = RequestMethod.GET)
    public String forgetGet(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {

        return "forget";
    }

    @RequestMapping(value = "forget", method = RequestMethod.POST)
    public String forget(@Valid UserBean loginBean, BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            System.err.println(result.toString() + "   " + (result.getAllErrors()).get(0).toString());
            return null;
        }

        String userEmail = loginBean.getUserEmail();

        UserDao userDao = new UserDao();
        User user = userDao.getUserByEmail(userEmail);
        if (user == null) {
            model.addAttribute("mailErrMsg", "用户不存在");
            return "forget";
        }
        //先用简单的方法发送邮件
        String tmpl = "<html>亲爱的${username}："
                + "<br/><pre>"
                + "  您在阿土游戏的密码为：${password}<br/>"
                + "  阿土建议您再次登录后进入个人中心修改密码，以防密码泄露。<br/>"
                + "  美好的游戏人生，阿土与您共度：）<br/>"
                + "  阿土游戏<br/>"
                + "  <a href=\"http://www.atugame.com\">www.atugame.com</a>"
                + "</pre></html>";

        try {
            EmailSenderDriver esd = new EmailSenderDriver();
            esd.setTo(userEmail, userEmail);
            esd.setFrom("topheytest@163.com", "阿土游戏");
            esd.setSubject("密码邮件");
            tmpl =tmpl.replace("${username}", user.getUsername()).replace("${password}", user.getPassword());            
            esd.setBody(tmpl);
            esd.setSMTPHost("smtp.163.com", "topheytest@163.com", "winnie");
            esd.setSMTPPort(25);
            esd.setTimeout(1000 * 30);
//        esd.setSSLEnable();
            esd.sendMsg();
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("mailErrMsg", "发送失败，请重试");
        }

        //TODO session相关
        return "redirect:/index.htm";
    }

    @RequestMapping(value = "editPwdSuc", method = RequestMethod.GET)
    public String editPwdSuc(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {

        return "editPwdSuc";
    }
    
    @RequestMapping(value = "editPassword", method = RequestMethod.GET)
    public String editPwdGet(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {

        return "editPwd";
    }

    @RequestMapping(value = "editPassword", method = RequestMethod.POST)
    public String editPwd(@Valid UserBean loginBean, BindingResult result,HttpServletRequest request, HttpServletResponse response,
            Model model) {

        
        String username = (String) request.getSession().getAttribute(SessionConst.USERNAME);
        if(username == null){
            return "redirect:/user/login.htm";
        }
        

        UserDao userDao = new UserDao();
        User user = userDao.getUserByEmail(username);
        if(!user.getPassword().equals(loginBean.getOldPassword())){
            model.addAttribute("oldPwdErrMsg", "旧密码错误");
            return "editPwd";
        }
        user.setPassword(loginBean.getPassword());
        userDao.udpateUser(user);
        
        return "redirect:/user/editPwdSuc.htm";
    }
    
    
    
    @RequestMapping("check")
    public @ResponseBody
    boolean check(HttpServletRequest request,
            HttpServletResponse response, Model model) {

        String userEmail = request.getParameter("userEmail");
        User user = new UserDao().getUserByEmail(userEmail);
        if (user == null) {
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping("checki")
    public @ResponseBody
    boolean checki(HttpServletRequest request,
            HttpServletResponse response, Model model) {

        return !check(request, response, model);

    }
}
