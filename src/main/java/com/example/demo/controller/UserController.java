package com.example.demo.controller;

import com.example.demo.controller.viewobject.UserVO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException{
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取到的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    //定义exceptionhandler 解决未被controller层吸收的exception异常
    @ExceptionHandler(Exception.class)   //定义根类
    @ResponseStatus(HttpStatus.OK)  //加载自定义异常类    HttpStatus.OK是http状态码
    @ResponseBody
    public Object handlerException(HttpServletRequest request,Exception ex){
        BusinessException businessException = (BusinessException)ex;
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("errCode",businessException.getErrCode());
        responseData.put("errMsg",businessException.getErrMsg());
        commonReturnType.setData(responseData);
        return commonReturnType;
    }
}
