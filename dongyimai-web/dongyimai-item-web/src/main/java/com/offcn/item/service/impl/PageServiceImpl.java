package com.offcn.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.entity.Result;
import com.offcn.item.service.PageService;
import com.offcn.sellergoods.feign.GoodsFeign;
import com.offcn.sellergoods.feign.ItemCatFeign;
import com.offcn.sellergoods.group.GoodsEntity;
import com.offcn.sellergoods.pojo.Goods;
import com.offcn.sellergoods.pojo.GoodsDesc;
import com.offcn.sellergoods.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private ItemCatFeign itemCatFeign;

    @Value("${pagepath}")
    private String pagepath;

    /**
     * //准备数据
     * 构建数据模型
     *
     * @param spuId
     * @return
     */
    private Map<String, Object> buildDataModel(Long spuId) {
        //构建数据模型
        Map<String, Object> dataMap = new HashMap<>();
        //获取SPU 和SKU列表
        Result<GoodsEntity> result = goodsFeign.findById(spuId);
        GoodsEntity goodsEntity = result.getData();
        //1.加载SPU数据
        Goods goods = goodsEntity.getGoods();
        //2.加载商品扩展数据
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();

        //3.加载SKU数据
        List<Item> itemList = goodsEntity.getItemList();

        dataMap.put("goods", goods);

        dataMap.put("goodsDesc", goodsDesc);
        dataMap.put("specificationList", JSON.parseArray(goodsDesc.getSpecificationItems(), Map.class));
        dataMap.put("imageList", JSON.parseArray(goodsDesc.getItemImages(), Map.class));
        dataMap.put("itemList", itemList);

        //4.加载分类数据
        dataMap.put("category1", itemCatFeign.findById(goods.getCategory1Id().longValue()).getData());
        dataMap.put("category2", itemCatFeign.findById(goods.getCategory2Id().longValue()).getData());
        dataMap.put("category3", itemCatFeign.findById(goods.getCategory3Id().longValue()).getData());


        return dataMap;
    }

    @Override
    public void createPageHtml(Long spuId) {

        //准备数据
        Map<String, Object> dataModel = buildDataModel(spuId);
        Context context = new Context();
        context.setVariables(dataModel);

        //准备字符输出流
        File dir = new File(pagepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "\\" + spuId + ".html");
        try {
            PrintWriter printWriter = new PrintWriter(file, "UTF-8");
            //准备模板
            templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
