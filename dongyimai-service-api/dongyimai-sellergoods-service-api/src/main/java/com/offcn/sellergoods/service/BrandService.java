package com.offcn.sellergoods.service;

import com.offcn.entity.PageResult;
import com.offcn.sellergoods.pojo.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    /***
     * 查询所有品牌
     * @return
     */
    List<Brand> findAll();
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Brand findById(Long id);

    /***
     * 新增品牌
     * @param brand
     */
    void add(Brand brand);
    /***
     * 修改品牌数据
     * @param brand
     */
    void update(Brand brand);


    /***
     * 删除品牌
     * @param id
     */
    void delete(Long id);

    /***
     * 多条件搜索品牌方法
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    public PageResult<Brand> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageResult<Brand> findPage(Brand brand, int page, int size);

    /**
     * 查询品牌下拉列表
     * @return
     */
    public List<Map> selectOptions();
}