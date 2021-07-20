package com.example.demo;

import com.example.demo.DataObject.UserDo;
import com.example.demo.dao.UserDoMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.example.demo"})    //等价于以默认属性使用@Configuration ， @EnableAutoConfiguration 和 @ComponentScan
//@EnableAutoConfiguration  //开启自动配置
@RestController
@MapperScan("com.example.demo.dao")
public class DemoApplication {
// 测试git
    @Autowired
    private UserDoMapper userDoMapper;

    @RequestMapping("/")
    public String home(){
        UserDo userDo = userDoMapper.selectByPrimaryKey(1);
        if (userDo == null){
            return "用户对象不存在";
        }else {
            return userDo.getName();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
