package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.DataObject.ItemDo;
import com.example.demo.DataObject.ItemStockDo;
import com.example.demo.dao.ItemDoMapper;
import com.example.demo.dao.ItemStockDoMapper;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.service.ItemService;
import com.example.demo.service.model.ItemModel;
import com.example.demo.validator.ValidationResult;
import com.example.demo.validator.ValidatorImpl;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    private ItemDo convertItemDoFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDo);
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        return itemDo;
    }

    private ItemStockDo convertItemStockDoFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStock());
        return itemStockDo;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化itemmodel -> dataobject
        ItemDo itemDo = this.convertItemDoFromItemModel(itemModel);

        //写入数据库
        itemDoMapper.insertSelective(itemDo);
        itemModel.setId(itemDo.getId());
        ItemStockDo itemStockDo = this.convertItemStockDoFromItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    //商品列表浏览
    public List<ItemModel> listItem() {
        List<ItemDo> itemDo = itemDoMapper.listItem();
        List<ItemModel> itemModelList = new ArrayList<>();
        System.out.println(itemDo.get(0).getId());

        for (int i=0;i<itemDo.size();i++){
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.get(i).getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDo.get(i), itemStockDo);
            itemModelList.add(itemModel);
        }


//            List<ItemModel> itemModelList = itemDoList.stream().map(itemDo -> {
//                ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
//                ItemModel itemModel = this.convertModelFromDataObject(itemDo, itemStockDo);
//                return itemModel;
//            }).collect(Collectors.toList());
//        stream(): 把一个源数据，可以是集合，数组，I/O channel，产生器generator等，转化成流
//        map(): 用于映射每个元素到对应的结果
//        Collectors.toList() 用来结束Stream流
        return itemModelList;
    }



    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null){
            return null;
        }
        //操作获取库存数量
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());

        //将dataobject -> model
        ItemModel itemModel = convertModelFromDataObject(itemDo, itemStockDo);
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDoMapper.decreaseStock(itemId,amount);
        //返回影响条目数 修改库存值成功返回1 不成功返回0
        if (affectedRow > 0){
            //更新库存成功
            return true;
        }else {
            //更新库存失败
            return false;
        }
    }

    //商品销量增加
    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDoMapper.increaseSales(itemId,amount);
    }

    private ItemModel convertModelFromDataObject(ItemDo itemDo,ItemStockDo itemStockDo){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo,itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        itemModel.setStock(itemStockDo.getStock());
        return  itemModel;
    }
}
