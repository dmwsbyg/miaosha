package com.example.demo.controller;

import com.alibaba.druid.util.StringUtils;
import com.example.demo.controller.viewobject.UserVO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
//@CrossOrigin  //解决跨越问题
//@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")  //做session共享的跨越请求
//@CrossOrigin(origins = {"http://localhost:8080", "null"})
//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"http://127.0.0.1:8080/"})
public class UserController extends BaseController{   //如果不继承BaseController类的话抛出的异常不会被BaseController类捕获

    @Autowired
    private UserService userService;

    @Autowired   //初始化对象
    private HttpServletRequest httpServletRequest;


    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() throws Exception{
//        String s = null;
//        s.equals("1");
//        try {
//
//        }

        if(true) {
            throw new Exception("hello world");
        }
        return "sbsbsbsbsb";
    }


    //用户登入接口
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //入参校验 手机号 密码不能为空
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登入服务，用来校验用户登入是否合法
        UserModel userModel = userService.validateLogin(telphone,this.EncodeByMd5(password));

        //将登入凭证加入到用户登入成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }


    //用户注册接口
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpcode相符合
        String inSessionOtpCode = (String) this.httpServletRequest
                .getSession()
                .getAttribute(telphone);
        if (!StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password)); //MD5Encoder给密码加密

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);   //生成一个0到99999的随机数
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);


        //将OTP验证码同对应用户的手机号关联,使用httpsession的方式绑定他的手机号与otpcode 比如现在 断点是在这里， 用step over会执行到下面的system.out
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

// 点step into就会进入到你这行代码调用的方法里面，比如现在这行代码调用的是setAttribute方法
        //将OTP验证码通过短信通道发送给用户,省略
        System.out.println("telphone = "+telphone+" & otpCode = "+otpCode);


        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody   //将返回值以特定格式写入到response的body区域，进而将数据返回给客户端
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException{
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取到的对应用户信息不存在
        if(userModel == null){
//            userModel.setEncrptPassword("123");
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
        //也可以用以下的getset方法 BeanUtils.copyProperties方法中userVo里有但是UserModel里没有的时候不能用 字段名不一样的时候也不能用
//        userVO.setId(userModel.getId());
//        userVO.setName(userModel.getName());
//        userVO.setGender(userModel.getGender());
//        userVO.setAge(userModel.getAge());
//        userVO.setTelphone(userModel.getTelphone());
        return userVO;
    }


}
