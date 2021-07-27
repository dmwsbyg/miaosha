package com.example.demo.service.impl;

import com.example.demo.DataObject.UserDo;
import com.example.demo.DataObject.UserPasswordDo;
import com.example.demo.dao.UserDoMapper;
import com.example.demo.dao.UserPasswordDoMapper;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Override  //
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

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        System.out.println("StringUtils.isEmpty(userModel.getTelphone())="+StringUtils.isEmpty(userModel.getTelphone()));//判断对象是否为空
        System.out.println("StringUtils.isNotEmpty(userModel.getName()="+StringUtils.isEmpty(userModel.getName()));
        if (StringUtils.isEmpty(userModel.getName())
        || userModel.getGender() == null
        || userModel.getAge() == null
        || StringUtils.isEmpty(userModel.getTelphone())){
            throw new BusinessException((EmBusinessError.PARAMETER_VALIDATION_ERROR));
        }
//        UserDo userDo = new UserDo();

        //实现model->dataobject方法
        UserDo userDo = convertFromModel(userModel);
        try{
            userDoMapper.insertSelective(userDo);//用insertSelective方法在.xml文件中插入时会自动判断字段是否为空，为空的话会使用数据库默认值不会用null
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该手机号已注册");
        }

        userModel.setId(userDo.getId());
//        if ()
//        String s = null;
//        s.equals("1");
        UserPasswordDo userPasswordDo = convertPasswordFromModel(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDo);
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户手机号获取用户信息
        UserDo userDo = userDoMapper.selectByTelphone(telphone);
        if (userDo == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());
        UserModel userModel = convertFromDataObject(userDo,userPasswordDo);

        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if (!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    private UserPasswordDo convertPasswordFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPasswordDo userPasswordDo = new UserPasswordDo();
        userPasswordDo.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDo.setUserId(userModel.getId());
        return userPasswordDo;
    }

    private UserDo convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel,userDo);

        return userDo;
    }

    private UserModel convertFromDataObject(UserDo userDo, UserPasswordDo userPasswordDo){
        if (userDo == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo,userModel);//从userDo对象复制属性到userModel对象


        if (userPasswordDo != null){
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }
        return userModel;
    }
}
