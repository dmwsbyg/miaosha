package com.example.demo.service.impl;

import com.example.demo.DataObject.PromoDo;
import com.example.demo.dao.PromoDoMapper;
import com.example.demo.service.PromoService;
import com.example.demo.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDoMapper promoDoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoDo promoDo = promoDoMapper.selectByItemId(itemId);

        //datObject -> model
        PromoModel promoModel = convertFromDataObject(promoDo);
        if (promoModel == null){
            return null;
        }

        //判断当前时间是否秒杀活动即将开始或正在进行
//        DateTime now = new DateTime();
        if (promoModel.getStartDate().isAfterNow()){  //开始时间比现在晚 未开始
            promoModel.setStatus(1);
        }else if (promoModel.getEndDate().isBeforeNow()){  //结束时间比现在还前 已结束
            promoModel.setStatus(3);
        }else {  //其余为 进行中
            promoModel.setStatus(2);
        }
        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoDo promoDo){
        if (promoDo == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDo.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDo.getEndDate()));

        return promoModel;
    }
}
