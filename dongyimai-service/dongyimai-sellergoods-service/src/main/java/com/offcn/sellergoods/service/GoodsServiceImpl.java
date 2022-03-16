package com.offcn.sellergoods.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.*;
import com.offcn.sellergoods.group.GoodsEntity;
import com.offcn.sellergoods.pojo.*;
import com.offcn.sellergoods.service.GoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Goods业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
@Transactional
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {


    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GoodsDescMapper goodsDescMapper;
    @Resource
    private ItemMapper itemMapper;
    @Resource
    private ItemCatMapper itemCatMapper;
    @Resource
    private BrandMapper brandMapper;

    /**
     * Goods条件+分页查询
     *
     * @param goods 查询条件
     * @param page  页码
     * @param size  页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Goods> findPage(Goods goods, int page, int size) {
        Page<Goods> mypage = new Page<>(page, size);
        QueryWrapper<Goods> queryWrapper = this.createQueryWrapper(goods);
        IPage<Goods> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Goods>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Goods分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Goods> findPage(int page, int size) {
        Page<Goods> mypage = new Page<>(page, size);
        IPage<Goods> iPage = this.page(mypage, new QueryWrapper<Goods>());

        return new PageResult<Goods>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Goods条件查询
     *
     * @param goods
     * @return
     */
    @Override
    public List<Goods> findList(Goods goods) {
        //构建查询条件
        QueryWrapper<Goods> queryWrapper = this.createQueryWrapper(goods);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Goods构建查询对象
     *
     * @param goods
     * @return
     */
    public QueryWrapper<Goods> createQueryWrapper(Goods goods) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (goods != null) {
            // 主键
            if (!StringUtils.isEmpty(goods.getId())) {
                queryWrapper.eq("id", goods.getId());
            }
            // 商家ID
            if (!StringUtils.isEmpty(goods.getSellerId())) {
                queryWrapper.eq("seller_id", goods.getSellerId());
            }
            // SPU名
            if (!StringUtils.isEmpty(goods.getGoodsName())) {
                queryWrapper.eq("goods_name", goods.getGoodsName());
            }
            // 默认SKU
            if (!StringUtils.isEmpty(goods.getDefaultItemId())) {
                queryWrapper.eq("default_item_id", goods.getDefaultItemId());
            }
            // 状态
            if (!StringUtils.isEmpty(goods.getAuditStatus())) {
                queryWrapper.eq("audit_status", goods.getAuditStatus());
            }
            // 是否上架
            if (!StringUtils.isEmpty(goods.getIsMarketable())) {
                queryWrapper.eq("is_marketable", goods.getIsMarketable());
            }
            // 品牌
            if (!StringUtils.isEmpty(goods.getBrandId())) {
                queryWrapper.eq("brand_id", goods.getBrandId());
            }
            // 副标题
            if (!StringUtils.isEmpty(goods.getCaption())) {
                queryWrapper.eq("caption", goods.getCaption());
            }
            // 一级类目
            if (!StringUtils.isEmpty(goods.getCategory1Id())) {
                queryWrapper.eq("category1_id", goods.getCategory1Id());
            }
            // 二级类目
            if (!StringUtils.isEmpty(goods.getCategory2Id())) {
                queryWrapper.eq("category2_id", goods.getCategory2Id());
            }
            // 三级类目
            if (!StringUtils.isEmpty(goods.getCategory3Id())) {
                queryWrapper.eq("category3_id", goods.getCategory3Id());
            }
            // 小图
            if (!StringUtils.isEmpty(goods.getSmallPic())) {
                queryWrapper.eq("small_pic", goods.getSmallPic());
            }
            // 商城价
            if (!StringUtils.isEmpty(goods.getPrice())) {
                queryWrapper.eq("price", goods.getPrice());
            }
            // 分类模板ID
            if (!StringUtils.isEmpty(goods.getTypeTemplateId())) {
                queryWrapper.eq("type_template_id", goods.getTypeTemplateId());
            }
            // 是否启用规格
            if (!StringUtils.isEmpty(goods.getIsEnableSpec())) {
                queryWrapper.eq("is_enable_spec", goods.getIsEnableSpec());
            }
            // 是否删除
            if (!StringUtils.isEmpty(goods.getIsDelete())) {
                queryWrapper.eq("is_delete", goods.getIsDelete());
            }
        }
        return queryWrapper;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Goods goods = goodsMapper.selectById(id);
        if ("1".equals(goods.getIsMarketable())){
            throw new IllegalStateException("必须先下架再删除！");
        }
        goods.setAuditStatus("0");
        goods.setIsDelete("1");
        goodsMapper.updateById(goods);
    }

    /**
     * 修改Goods
     *
     * @param goodsEntity
     */
    @Override
    public void update(GoodsEntity goodsEntity) {
        Goods goods = goodsEntity.getGoods();
        goods.setAuditStatus("0");
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        List<Item> itemList = goodsEntity.getItemList();
        this.updateById(goods);
        goodsDescMapper.updateById(goodsDesc);
        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("goods_id", goods.getId());
        itemMapper.delete(itemQueryWrapper);
        this.saveItemList(goods, goodsDesc, itemList);
    }


    /**
     * 根据ID查询Goods
     *
     * @param id
     * @return
     */
    @Override
    public GoodsEntity findById(Long id) {
        Goods goods = this.getById(id);
        GoodsDesc goodsDesc = goodsDescMapper.selectById(id);
        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("goods_id", id);
        List<Item> itemList = itemMapper.selectList(itemQueryWrapper);
        return new GoodsEntity(goods, goodsDesc, itemList);
    }

    /**
     * 查询Goods全部数据
     *
     * @return
     */
    @Override
    public List<Goods> findAll() {
        return this.list(new QueryWrapper<Goods>());
    }

    /***
     * 新增Goods
     * @param goodsEntity
     */
    @Override
    public void add(GoodsEntity goodsEntity) {
        Goods goods = goodsEntity.getGoods();
        goods.setAuditStatus("0");
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        List<Item> itemList = goodsEntity.getItemList();
        goodsMapper.insert(goods);
        goodsDesc.setGoodsId(goods.getId());
        goodsDescMapper.insert(goodsDesc);
        this.saveItemList(goods, goodsDesc, itemList);
    }

    /**
     * 下架商品
     * @param goodsId
     */
    @Override
    public void audit(Long goodsId) {
        Goods goods = this.getById(goodsId);
        if ("1".equals(goods.getIsDelete())) {
            throw new IllegalStateException("该商品已删除");
        }
        goods.setAuditStatus("1");
        goods.setIsMarketable("1");
        goodsMapper.updateById(goods);
    }

    /**
     *
     * @param goodsId
     */
    @Override
    public void pull(Long goodsId) {
        Goods goods = this.getById(goodsId);
        if ("1".equals(goods.getIsDelete())) {
            throw new IllegalStateException("该商品已删除");
        }
        goods.setIsMarketable("0");
        goodsMapper.updateById(goods);
    }

    /**
     * 上架商品
     * @param goodsId
     */
    @Override
    public void put(Long goodsId) {
        Goods goods = this.getById(goodsId);
        if ("1".equals(goods.getIsDelete())) {
            throw new IllegalStateException("该商品已删除");
        }
        if (!"1".equals(goods.getAuditStatus())) {
            throw new IllegalStateException("该商品未审核");
        }
        goods.setIsMarketable("1");
        this.updateById(goods);
    }

    /**
     * 批量上架商品
     * @param ids
     * @return
     */
    @Override
    public int putMany(Long[] ids) {
        Goods goods = new Goods();
        goods.setIsMarketable("1");
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.in("id", Arrays.asList(ids));
        goodsQueryWrapper.eq("audit_status","1");
        goodsQueryWrapper.ne("is_delete","1");
        return goodsMapper.update(goods,goodsQueryWrapper);
    }

    private void saveItemList(Goods goods, GoodsDesc goodsDesc, List<Item> itemList) {
        if ("1".equals(goods.getIsEnableSpec())) {
            if (CollectionUtils.isNotEmpty(itemList)) {
                for (Item item : itemList) {
                    String title = goods.getGoodsName();
                    Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                    for (String key : map.keySet()) {
                        title += map.get(key);
                    }
                    item.setTitle(title);
                    this.setItemInfo(goods, goodsDesc, item);
                    itemMapper.insert(item);
                }
            }
        } else {
            //不启用规格  SKU信息为默认值
            Item item = new Item();
            item.setTitle(goods.getGoodsName());
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            item.setStatus("1");
            item.setIsDefault("1");
            item.setSpec("{}");
            this.setItemInfo(goods, goodsDesc, item);
            itemMapper.insert(item);
        }
    }

    private void setItemInfo(Goods goods, GoodsDesc goodsDesc, Item item) {
        item.setCategoryId(goods.getCategory3Id());
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setGoodsId(goods.getId());
        item.setSellerId(goods.getSellerId());
        ItemCat itemCat = itemCatMapper.selectById(goods.getCategory3Id());
        item.setCategory(itemCat.getName());
        Brand brand = brandMapper.selectById(goods.getBrandId());
        item.setBrand(brand.getName());
        List<Map> imageList = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
        if (imageList.size() > 0) {
            item.setImage((String) imageList.get(0).get("url"));
        }
    }
}
