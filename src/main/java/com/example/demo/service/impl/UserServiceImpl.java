package com.example.demo.service.impl;

import com.example.demo.DataObject.UserDo;
import com.example.demo.DataObject.UserPasswordDo;
import com.example.demo.dao.UserDoMapper;
import com.example.demo.dao.UserPasswordDoMapper;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdomapper获取对应的用户dataobject
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);
        if (userDo == null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());
        return convertFromDataObject(userDo,userPasswordDo);
    }

    private UserModel convertFromDataObject(UserDo userDo, UserPasswordDo userPasswordDo){
        if (userDo == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo,userModel);//从userModel对象复制属性到userDo对象


        if (userPasswordDo != null){
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }
        return userModel;
    }
}
