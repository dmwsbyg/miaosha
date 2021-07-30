package com.example.demo.controller;


import com.example.demo.controller.viewobject.ItemVO;
import com.example.demo.error.BusinessException;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.ItemService;
import com.example.demo.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller("/item")
@RequestMapping("/item")
public class ItemController extends BaseController{

    @Autowired
    private ItemService itemService;

    //创建商品的controller
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price")BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);

        ItemVO itemVO = convertVoFromModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertVoFromModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }
}
