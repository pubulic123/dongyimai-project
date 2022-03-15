package com.offcn.search.service;

import com.alibaba.fastjson.JSON;
import com.offcn.entity.Result;
import com.offcn.search.dao.SkuEsMapper;
import com.offcn.search.pojo.SkuInfo;
import com.offcn.sellergoods.feign.ItemFeign;
import com.offcn.sellergoods.pojo.Item;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ItemFeign itemFeign;

    @Override
    public void importSku() {

        //调用商品微服务，获取sku商品数据
        Result<List<Item>> result = itemFeign.findByStatus("1");
        //把数据转换为搜索实体类数据
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(result.getData()), SkuInfo.class);

        //遍历sku集合
        for (SkuInfo skuInfo : skuInfoList) {
            //获取规格
          Map<String,Object> specMap= JSON.parseObject(skuInfo.getSpec());
          //关联设置到specMap
            skuInfo.setSpecMap(specMap);
        }

        //保存sku集合数据到es
        skuEsMapper.saveAll(skuInfoList);
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public Map search(Map<String, String> searchMap) {

        //1.获取关键字的值
        String keywords = searchMap.get("keywords");

        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";//赋值给一个默认的值
        }
        //2.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        // 设置分组的条件  terms后表示分组查询后的列名
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("category"));
        //3.设置查询的条件
        //使用：QueryBuilders.matchQuery("title", keywords) ，搜索华为 ---> 华    为 二字可以拆分查询，
        //使用：QueryBuilders.matchPhraseQuery("title", keywords) 华为二字不拆分查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title", keywords));

        //4.构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //5.执行查询
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(query, SkuInfo.class);
        //对搜索searchHits集合进行分页封装
        SearchPage<SkuInfo> skuPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        //获取分组结果
        Terms terms = searchHits.getAggregations().get("skuCategorygroup");
        // 获取分类名称集合
        List<String> categoryList = this.getStringsCategoryList(terms);
        //遍历取出查询的商品信息
        List<SkuInfo> skuList=new ArrayList<>();
        for (SearchHit<SkuInfo> searchHit :skuPage.getContent()) { // 获取搜索到的数据
            SkuInfo content = (SkuInfo) searchHit.getContent();
            SkuInfo skuInfo = new SkuInfo();
            BeanUtils.copyProperties(content, skuInfo);
            skuList.add(skuInfo);
        }

        //6.返回结果
        Map resultMap = new HashMap<>();
        resultMap.put("rows", skuList);//获取所需SkuInfo集合数据内容
        resultMap.put("total",searchHits.getTotalHits());//总记录数
        resultMap.put("totalPages", skuPage.getTotalPages());//总页数
        resultMap.put("categoryList",categoryList);
        return resultMap;
    }

    /**
     * 获取分类列表数据
     * @param terms
     * @return
     **/
    private List<String> getStringsCategoryList(Terms terms) {
        List<String> categoryList = new ArrayList<>();

        if (terms != null) {
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();// 分组的值（分类名称）
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }
}
