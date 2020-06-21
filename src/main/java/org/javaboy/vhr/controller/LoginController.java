package org.javaboy.vhr.controller;

import org.javaboy.vhr.utils.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("login")
    public ServerResponse login(){
        return ServerResponse.error(2000,"用户未登录");
    }
}
