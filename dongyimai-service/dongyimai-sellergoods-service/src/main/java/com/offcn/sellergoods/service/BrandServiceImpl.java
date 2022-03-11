package com.offcn.sellergoods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.BrandMapper;
import com.offcn.sellergoods.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

//品牌服务
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Reference
    private BrandMapper brandMapper;
    /**
     * 全部数据
     * @return
     */
    public List<Brand> findAll(){
        return this.list();
    }
    /**
     * 根据ID查询Brand
     * @param id
     * @return
     */
    @Override
    public Brand findById(Long id) {
        return  this.getById(id);
    }

    /**
     * 增加Brand
     * @param brand
     */
    @Override
    public void add(Brand brand){
        this.save(brand);
    }

    /**
     * 修改Brand
     * @param brand
     */
    @Override
    public void update(Brand brand){
        this.updateById(brand);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        this.removeById(id);
    }

    /**
     * Brand条件查询
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findList(Brand brand){
        //构建查询条件
        QueryWrapper<Brand> queryWrapper = this.createQueryWrapper(brand);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Brand构建查询对象
     * @param brand
     * @return
     */
    private QueryWrapper<Brand> createQueryWrapper(Brand brand){
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        if(brand!=null){
            //
            if(brand.getId()!=null){
                queryWrapper.eq("id",brand.getId());
            }
            // 品牌名称
            if(!StringUtils.isEmpty(brand.getName())){
                queryWrapper.like("name",brand.getName());
            }
            // 品牌首字母
            if(!StringUtils.isEmpty(brand.getFirstChar())){
                queryWrapper.eq("first_char",brand.getFirstChar());
            }
            // 品牌图像
            if(!StringUtils.isEmpty(brand.getImage())){
                queryWrapper.eq("image",brand.getImage());
            }
        }
        return queryWrapper;
    }

    /**
     * Brand分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Brand> findPage(int page, int size){
        Page<Brand> mypage = new Page<>(page, size);
        IPage<Brand> iPage = this.page(mypage, new QueryWrapper<Brand>());

        return new PageResult<Brand>(iPage.getTotal(),iPage.getRecords());
    }
    /**
     * Brand条件+分页查询
     * @param brand 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Brand> findPage(Brand brand, int page, int size){
        Page<Brand> mypage = new Page<>(page, size);
        QueryWrapper<Brand> queryWrapper = this.createQueryWrapper(brand);
        IPage<Brand> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Brand>(iPage.getTotal(),iPage.getRecords());
    }

    @Override
    public List<Map> selectOptions() {
        return brandMapper.selectOptions();
    }


}