package com.offcn.sellergoods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.BrandMapper;
import com.offcn.sellergoods.pojo.Brand;
import com.offcn.sellergoods.service.BrandService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Resource
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return this.list();
    }

    @Override
    public Brand findById(int id) {
        return this.getById(id);
    }

    @Override
    public void add(Brand brand) {
        this.save(brand);
    }

    @Override
    public void update(Brand brand) {
        this.updateById(brand);
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    public List<Brand> findList(Brand brand) {
        QueryWrapper<Brand> queryWrapper = this.createQueryWrapper(brand);
        return this.list(queryWrapper);
    }

    @Override
    public PageResult<Brand> findPage(int page, int size) {
        Page<Brand> brandPage = new Page<>(page, size);
        Page<Brand> myPage = this.page(brandPage);
        return new PageResult<>(myPage.getTotal(), myPage.getRecords());
    }

    @Override
    public PageResult<Brand> findPage(Brand brand, int page, int size) {
        QueryWrapper<Brand> queryWrapper = this.createQueryWrapper(brand);
        Page<Brand> brandPage = new Page<>(page, size);
        Page<Brand> myPage = this.page(brandPage, queryWrapper);
        return new PageResult<>(myPage.getTotal(), myPage.getRecords());
    }

    @Override
    public List<Map<String, String>> selectOptions() {
        return brandMapper.selectOptions();
    }

    /**
     * 跟据brand创建QueryWrapper
     *
     * @param brand
     * @return
     */
    private QueryWrapper<Brand> createQueryWrapper(Brand brand) {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        if (brand.getId() != null) {
            queryWrapper.eq("id", brand.getId());
        }
        if (!StringUtils.isNullOrEmpty(brand.getName())) {
            queryWrapper.like("name", brand.getName());
        }
        if (!StringUtils.isNullOrEmpty(brand.getFirstChar())) {
            queryWrapper.eq("first_char", brand.getFirstChar());
        }
        if (!StringUtils.isNullOrEmpty(brand.getImage())) {
            queryWrapper.eq("image", brand.getImage());
        }
        return queryWrapper;
    }
}
