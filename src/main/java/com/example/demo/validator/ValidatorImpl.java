package com.example.demo.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import java.util.Set;

@Component  //把它归类为一个bean 进行类扫描的时候会扫描到它
public class ValidatorImpl implements InitializingBean {

    private javax.validation.Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        //用final修饰 地址不能修改，但是对象本身的属性可以修改
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
//        validator.validate(bean);
        if (constraintViolationSet.size() >0){
            //有错误
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator 通过工厂的初始化方式使其实例化
//
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
