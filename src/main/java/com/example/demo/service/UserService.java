package com.example.demo.service;

import com.example.demo.error.BusinessException;
import com.example.demo.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
}
