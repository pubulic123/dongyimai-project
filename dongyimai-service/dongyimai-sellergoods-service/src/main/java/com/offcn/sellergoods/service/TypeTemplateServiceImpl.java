package com.offcn.sellergoods.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.SpecificationOptionMapper;
import com.offcn.sellergoods.dao.TypeTemplateMapper;
import com.offcn.sellergoods.pojo.SpecificationOption;
import com.offcn.sellergoods.pojo.TypeTemplate;
import com.offcn.sellergoods.service.TypeTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:TypeTemplate业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class TypeTemplateServiceImpl extends ServiceImpl<TypeTemplateMapper,TypeTemplate> implements TypeTemplateService {

    @Resource
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * TypeTemplate条件+分页查询
     * @param typeTemplate 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<TypeTemplate> findPage(TypeTemplate typeTemplate, int page, int size){
         Page<TypeTemplate> mypage = new Page<>(page, size);
        QueryWrapper<TypeTemplate> queryWrapper = this.createQueryWrapper(typeTemplate);
        IPage<TypeTemplate> iPage = this.page(mypage, queryWrapper);
        return new PageResult<TypeTemplate>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * TypeTemplate分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<TypeTemplate> findPage(int page, int size){
        Page<TypeTemplate> mypage = new Page<>(page, size);
        IPage<TypeTemplate> iPage = this.page(mypage, new QueryWrapper<TypeTemplate>());

        return new PageResult<TypeTemplate>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * TypeTemplate条件查询
     * @param typeTemplate
     * @return
     */
    @Override
    public List<TypeTemplate> findList(TypeTemplate typeTemplate){
        //构建查询条件
        QueryWrapper<TypeTemplate> queryWrapper = this.createQueryWrapper(typeTemplate);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * TypeTemplate构建查询对象
     * @param typeTemplate
     * @return
     */
    public QueryWrapper<TypeTemplate> createQueryWrapper(TypeTemplate typeTemplate){
        QueryWrapper<TypeTemplate> queryWrapper = new QueryWrapper<>();
        if(typeTemplate!=null){
            // 
            if(!StringUtils.isEmpty(typeTemplate.getId())){
                 queryWrapper.eq("id",typeTemplate.getId());
            }
            // 模板名称
            if(!StringUtils.isEmpty(typeTemplate.getName())){
                queryWrapper.like("name",typeTemplate.getName());
            }
            // 关联规格
            if(!StringUtils.isEmpty(typeTemplate.getSpecIds())){
                 queryWrapper.eq("spec_ids",typeTemplate.getSpecIds());
            }
            // 关联品牌
            if(!StringUtils.isEmpty(typeTemplate.getBrandIds())){
                 queryWrapper.eq("brand_ids",typeTemplate.getBrandIds());
            }
            // 自定义属性
            if(!StringUtils.isEmpty(typeTemplate.getCustomAttributeItems())){
                 queryWrapper.eq("custom_attribute_items",typeTemplate.getCustomAttributeItems());
            }
        }
        return queryWrapper;
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
     * 修改TypeTemplate
     * @param typeTemplate
     */
    @Override
    public void update(TypeTemplate typeTemplate){
        this.updateById(typeTemplate);
    }

    /**
     * 增加TypeTemplate
     * @param typeTemplate
     */
    @Override
    public void add(TypeTemplate typeTemplate){
        this.save(typeTemplate);
    }

    /**
     * 根据ID查询TypeTemplate
     * @param id
     * @return
     */
    @Override
    public TypeTemplate findById(Long id){
        return  this.getById(id);
    }

    /**
     * 查询TypeTemplate全部数据
     * @return
     */
    @Override
    public List<TypeTemplate> findAll() {
        return this.list(new QueryWrapper<TypeTemplate>());
    }

    /**
     * 查询规格(包含规格项)列表
     * @param id
     * @return
     */
    @Override
    public List<Map> findSpecList(Long id) {
        TypeTemplate typeTemplate = this.getById(id);
        List<Map> mapList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
        if (CollectionUtils.isNotEmpty(mapList)){
            for (Map map : mapList) {
                Long specId =new Long((Integer) map.get("id"));
                QueryWrapper<SpecificationOption> specificationOptionQueryWrapper = new QueryWrapper<>();
                specificationOptionQueryWrapper.eq("spec_id",specId);
                List<SpecificationOption> specificationOptionList = specificationOptionMapper.selectList(specificationOptionQueryWrapper);
                map.put("options",specificationOptionList);
            }
        }
        return mapList;
    }
}
