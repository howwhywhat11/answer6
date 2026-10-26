package com.test.number5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${user.usernamePattern}")
    String usernamePattern;
    @Value("${user.passwordMinLength}")
    int passwordMinLength;

    //用于加密密码
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register (String username,String password){
        //判断用户名重复
        User existingUsers = userMapper.findByUsername(username);
        if (existingUsers != null){
            throw new RuntimeException("用户名已存在");
        }
        //判断密码长度是否合法
        if(password.length() < passwordMinLength){
            throw new RuntimeException("密码长度至少为"+passwordMinLength+ "位");
        }
        //判断用户名是否合法
        if (!username.matches(usernamePattern)){
            throw new RuntimeException("用户名不合法");
        }
        //加密密码
        String EncodedPassword = passwordEncoder.encode(password);
        //添加用户名和密码
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(EncodedPassword);
        userMapper.insert(newUser);
    }

    public void login (String username,String password){
        User existingUser = userMapper.findByUsername(username);
        //用户名不存在时，为了安全仍然提示"用户名或密码错误"
        if (existingUser == null){
            throw new RuntimeException("用户名或密码错误");
        }
        String RealPassword = existingUser.getPassword();
        //密码错误时，提示"用户名或密码错误"
        if (!passwordEncoder.matches(password,RealPassword)){
            throw new RuntimeException("用户名或密码错误");
        }
    }

    public List<User> findByPage (int page, int size){
        int offset = (page-1)*size;
        return userMapper.findByPage(offset,size);
    }



}
