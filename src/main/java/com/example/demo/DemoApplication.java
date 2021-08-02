package com.example.demo;

import com.example.demo.DataObject.UserDo;
import com.example.demo.dao.UserDoMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = {"com.example.demo"})    //等价于以默认属性使用@Configuration ， @EnableAutoConfiguration 和 @ComponentScan
//扫描com.example.demo下的注释
//@EnableAutoConfiguration  //开启自动配置
@RestController
@ServletComponentScan   //删除之后不会执行CorsFilter  使用该注解后 Servler(控制器)， Filter(过滤器)， Listener(监听器) 可以直接通过 @WebServlet, @WebFilter, @WebListener注解自动注册到Spring容器中
@MapperScan("com.example.demo.dao")
public class DemoApplication {
// 测试git11
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

//
//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                           O\  =  /O
//                        ____/`---'\____
//                      .'  \\|     |//  `.
//                     /  \\|||  :  |||//  \
//                    /  _||||| -:- |||||-  \
//                    |   | \\\  -  /// |   |
//                    | \_|  ''\---/''  |   |
//                    \  .-\__  `-`  ___/-. /
//                  ___`. .'  /--.--\  `. . __
//               ."" '<  `.___\_<|>_/___.'  >'"".
//              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//              \  \ `-.   \_ __\ /__ _/   .-` /  /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//                      Buddha Bless, No Bug !

//
///**
// *　　　　　　　 ┏┓       ┏┓+ +
// *　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
// *　　　　　　　┃　　　　　　 ┃
// *　　　　　　　┃　　　━　　　┃ ++ + + +
// *　　　　　　 █████━█████  ┃+
// *　　　　　　　┃　　　　　　 ┃ +
// *　　　　　　　┃　　　┻　　　┃
// *　　　　　　　┃　　　　　　 ┃ + +
// *　　　　　　　┗━━┓　　　 ┏━┛
// *               ┃　　  ┃
// *　　　　　　　　　┃　　  ┃ + + + +
// *　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
// *　　　　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码无bug
// *　　　　　　　　　┃　　　┃
// *　　　　　　　　　┃　　　┃　　+
// *　　　　　　　　　┃　 　 ┗━━━┓ + +
// *　　　　　　　　　┃ 　　　　　┣┓
// *　　　　　　　　　┃ 　　　　　┏┛
// *　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
// *　　　　　　　　　 ┃┫┫　 ┃┫┫
// *　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
// */