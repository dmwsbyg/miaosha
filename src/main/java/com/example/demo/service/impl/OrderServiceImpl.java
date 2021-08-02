package com.example.demo.service.impl;

import com.example.demo.DataObject.OrderDo;
import com.example.demo.dao.OrderDoMapper;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.service.ItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.service.model.ItemModel;
import com.example.demo.service.model.OrderModel;
import com.example.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }

        if (amount <= 0 || amount > 99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }

        //落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if (!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice()); //购买时的单价
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount))); //订单总金额 单价*数量

        //生成交易单号
        OrderDo orderDo = convertFromOrderModel(orderModel);
        orderDoMapper.insertSelective(orderDo);

        //返回前端
        return null;
    }

    private String generateOrderNo(){
        //订单号16位
        StringBuilder stringBuilder = new StringBuilder(); //订单号16位拼接生成

        //前8位为时间信息
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        //replace 把 2021-08-23 这种时间类型中的 “ - ” 转化成空字符串 为 20210823 这种形式
        stringBuilder.append(nowDate);

        //中间6位为自增序列

        //最后两位分库分表位

        return null;
    }

    private OrderDo convertFromOrderModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel,orderDo);
        return orderDo;
    }
}
