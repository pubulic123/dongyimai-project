package com.offcn.sellergoods.service;

import com.offcn.entity.PageResult;
import com.offcn.sellergoods.pojo.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    /**
     * 查询所有Brand
     *
     * @return
     */
    List<Brand> findAll();

    /**
     * 根据ID查询brand
     *
     * @param id
     * @return
     */
    Brand findById(int id);

    /**
     * 根据实体brand持久化数据库(添加)
     *
     * @param brand
     */
    void add(Brand brand);

    /**
     * 根据实体brand持久化数据库(修改)
     *
     * @param brand
     */
    void update(Brand brand);

    /**
     * 根据id删除brand
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 根据brand创建条件模糊查询
     *
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /**
     * 无条件查询分页列表
     *
     * @param page
     * @param size
     * @return
     */
    PageResult<Brand> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageResult<Brand> findPage(Brand brand, int page, int size);

    /**
     * 查询下拉框列表
     *
     * @return
     */
    List<Map<String, String>> selectOptions();
}
