package com.cnpc.controller;

import com.cnpc.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    User user = new User();
    @RequestMapping(value = "/subLogin",method = RequestMethod.POST)
    @ResponseBody
    public String subLogin(){
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken( user.getUsername(),user.getPassword() );

        try{
            subject.login( token );
        }catch(Exception e){
            return e.getMessage();
        }
        return "登陆成功";
    }
}
