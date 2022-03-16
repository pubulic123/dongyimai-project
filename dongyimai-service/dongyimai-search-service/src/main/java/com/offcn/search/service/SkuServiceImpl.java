package com.offcn.search.service;

import com.alibaba.fastjson.JSON;
import com.offcn.entity.Result;
import com.offcn.search.dao.SkuEsMapper;
import com.offcn.search.pojo.SkuInfo;
import com.offcn.sellergoods.feign.ItemFeign;
import com.offcn.sellergoods.pojo.Item;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ItemFeign itemFeign;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /***
     * 导入SKU数据
     */
    @Override
    public void importSku() {

        //调用商品微服务，获取sku商品数据
        Result<List<Item>> result = itemFeign.findByStatus("1");
        //把数据转换为搜索实体类数据
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(result.getData()), SkuInfo.class);

        //遍历sku集合
        for (SkuInfo skuInfo : skuInfoList) {
            //获取规格
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec());
            //关联设置到specMap
            skuInfo.setSpecMap(specMap);
        }

        //保存sku集合数据到es
        skuEsMapper.saveAll(skuInfoList);
    }



    public Map search(Map<String, String> searchMap) {

        //1.获取关键字的值
        String keywords = searchMap.get("keywords");

        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";//赋值给一个默认的值
        }
        //2.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        // 设置分组的条件  terms后表示分组查询后的列名
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("category.keyword"));

        //设置分组条件  商品品牌
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brand.keyword"));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword"));
        //3.设置查询的条件
        //使用：QueryBuilders.matchQuery("title", keywords) ，搜索华为 ---> 华    为 二字可以拆分查询，
        //使用：QueryBuilders.matchPhraseQuery("title", keywords) 华为二字不拆分查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title", keywords));


        //========================过滤查询 开始=====================================
        //创建多条件组合查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //设置品牌查询条件
        if (!StringUtils.isEmpty(searchMap.get("brand"))) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("brand", searchMap.get("brand")));
        }
        //设置分类查询条件
        if (!StringUtils.isEmpty(searchMap.get("category"))) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("category", searchMap.get("category")));
        }
        //设置规格过滤查询
        if (searchMap != null) {
            for (String key : searchMap.keySet()) {//{ brand:"",category:"",spec_网络:"电信4G"}
                if (key.startsWith("spec_")) {
                    //截取规格的名称
                    boolQueryBuilder.filter(QueryBuilders.matchQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
                }
            }
        }
        //价格过滤查询
        String price = searchMap.get("price");
        if (!StringUtils.isEmpty(price)) {
            String[] split = price.split("-");
            if (!split[1].equalsIgnoreCase("*")) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0], true).to(split[1], true));
            } else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }
        }

        //关联过滤查询对象到查询器
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
        //========================过滤查询 结束=====================================

        //分页查询
        Integer pageNum = Integer.parseInt(searchMap.get("pageNum"));     //当前页码
        if (pageNum == null) {
            pageNum = 1;
        }
        Integer pageSize = 20;              //每页显示记录数


        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, pageSize));


        //构建排序查询
        String sortRule = searchMap.get("sortRule");
        String sortField = searchMap.get("sortField");
        if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equals("DESC") ? SortOrder.DESC : SortOrder.ASC));
        }

        //设置高亮条件
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("title"));
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style=\"color:red\">").postTags("</em>"));
        //设置主关键字查询,修改为多字段的搜索条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"title","brand","category"));

        //4.构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //5.执行查询
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(query, SkuInfo.class);

        //获取高亮结果
        for (SearchHit<SkuInfo> searchHit : searchHits) {
            //获取高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //获取标题的高亮结果
            searchHit.getContent().setTitle(highlightFields.get("title")==null?searchHit.getContent().getTitle():highlightFields.get("title").get(0));
        }
        //对搜索searchHits集合进行分页封装
        SearchPage<SkuInfo> skuPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());
        //创建集合存储商品信息
        List<SkuInfo> skuInfoList=new ArrayList<>();
        for (SearchHit<SkuInfo> searchHit : skuPage.getContent()) {
            SkuInfo skuInfo=     searchHit.getContent();
            skuInfoList.add(skuInfo);
        }


        //获取分组结果  商品分类
        Terms termCategory = searchHits.getAggregations().get("skuCategorygroup");

        //获取分组结果  商品分类
        Terms termBrand = searchHits.getAggregations().get("skuBrandgroup");

        //获取分组结果 商品规格
        Terms termsSpec = searchHits.getAggregations().get("skuSpecgroup");

        Map<String, Set<String>> specMap=getStringSetMap(termsSpec);
        // 获取分类名称集合
        List<String> categoryList = this.getStringsCategoryList(termCategory);
        // 获取分组名称集合
        List<String> brandList = this.getStringsCategoryList(termBrand);
        //遍历取出查询的商品信息
        List<SkuInfo> skuList=new ArrayList<>();


        //6.返回结果
        Map resultMap = new HashMap<>();
        resultMap.put("rows", skuList);//获取所需SkuInfo集合数据内容
        resultMap.put("total",searchHits.getTotalHits());//总记录数
        resultMap.put("totalPages", skuPage.getTotalPages());//总页数
        resultMap.put("categoryList",categoryList);
        resultMap.put("brandList",brandList);
        resultMap.put("specMap", specMap);
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

    /**
     * 获取品牌列表数据
     * @param termsBrand
     * @return
     **/
    private List<String> getStringsBrandList(Terms termsBrand) {
        List<String> brandList = new ArrayList<>();

        if (termsBrand != null) {
            for (Terms.Bucket bucket : termsBrand.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();// 分组的值（分类名称）
                brandList.add(keyAsString);
            }
        }
        return brandList;
    }


    /**
     * 获取规格列表数据
     *
     * @param termsSpec
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(Terms termsSpec) {
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
        Set<String> specList = new HashSet<>();
        if (termsSpec != null) {
            for (Terms.Bucket bucket : termsSpec.getBuckets()) {
                specList.add(bucket.getKeyAsString());
            }
        }
        for (String specjson : specList) {
            Map<String, String> map = JSON.parseObject(specjson, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {//
                String key = entry.getKey();        //规格名字
                String value = entry.getValue();    //规格选项值
                //获取当前规格名字对应的规格数据
                Set<String> specValues = specMap.get(key);
                if (specValues == null) {
                    specValues = new HashSet<String>();
                }
                //将当前规格加入到集合中
                specValues.add(value);
                //将数据存入到specMap中
                specMap.put(key, specValues);
            }
        }
        return specMap;
    }


}
