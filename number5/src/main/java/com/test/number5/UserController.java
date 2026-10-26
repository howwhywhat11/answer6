package com.test.number5;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    //实现 register 接口
    @PostMapping("/register")
    public Map<String,Object> register (@Valid @RequestBody UserRequest body){
        userService.register(body.getUsername(), body.getPassword());
        Map<String,Object> res= new HashMap<>();
        res.put("message","注册成功");
        return res;

    }

    //实现 login 接口
    @PostMapping("/login")
    public Map<String,Object> login (@Valid @RequestBody UserRequest body){
        userService.login(body.getUsername(), body.getPassword());
        Map<String,Object> res= new HashMap<>();
        res.put("message","success");
        return res;

    }

    @GetMapping("/users")
    public Map<String,Object> findUsers(@RequestParam(defaultValue = "1")int page,
                                        @RequestParam(defaultValue = "10")int size){
        List<User> data  =  userService.findByPage(page,size);
        Map<String,Object> res = new HashMap<>();
        res.put("code",200);
        res.put("message","查询成功");
        res.put("data",data);
        return res;
    }
}
