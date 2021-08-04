package com.example.demo.service;

import com.example.demo.service.model.PromoModel;

public interface PromoService {

    //根据itemId获取即将进行的或者正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
