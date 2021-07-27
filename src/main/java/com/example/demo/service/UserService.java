package com.example.demo.service;

import com.example.demo.error.BusinessException;
import com.example.demo.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;

    //telphone 为注册手机号  password为用户加密后的密码
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
