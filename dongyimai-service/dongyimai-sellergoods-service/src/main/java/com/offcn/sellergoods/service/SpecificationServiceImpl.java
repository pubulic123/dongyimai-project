package com.offcn.sellergoods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.SpecificationMapper;
import com.offcn.sellergoods.dao.SpecificationOptionMapper;
import com.offcn.sellergoods.group.SpecEntity;
import com.offcn.sellergoods.pojo.Specification;
import com.offcn.sellergoods.pojo.SpecificationOption;
import com.offcn.sellergoods.service.SpecificationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/****
 * @Author:ujiuye
 * @Description:Specification业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {


    @Resource
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * Specification条件+分页查询
     *
     * @param specification 查询条件
     * @param page          页码
     * @param size          页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Specification> findPage(Specification specification, int page, int size) {
        Page<Specification> mypage = new Page<>(page, size);
        QueryWrapper<Specification> queryWrapper = this.createQueryWrapper(specification);
        IPage<Specification> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Specification>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Specification分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Specification> findPage(int page, int size) {
        Page<Specification> mypage = new Page<>(page, size);
        IPage<Specification> iPage = this.page(mypage, new QueryWrapper<Specification>());

        return new PageResult<Specification>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * Specification条件查询
     *
     * @param specification
     * @return
     */
    @Override
    public List<Specification> findList(Specification specification) {
        //构建查询条件
        QueryWrapper<Specification> queryWrapper = this.createQueryWrapper(specification);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Specification构建查询对象
     *
     * @param specification
     * @return
     */
    public QueryWrapper<Specification> createQueryWrapper(Specification specification) {
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();
        if (specification != null) {
            // 主键
            if (!StringUtils.isEmpty(specification.getId())) {
                queryWrapper.eq("id", specification.getId());
            }
            // 名称
            if (!StringUtils.isEmpty(specification.getSpecName())) {
                queryWrapper.eq("spec_name", specification.getSpecName());
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
        QueryWrapper<SpecificationOption> specificationOptionQueryWrapper = new QueryWrapper<>();
        specificationOptionQueryWrapper.eq("spec_id",id);
        specificationOptionMapper.delete(specificationOptionQueryWrapper);
        this.removeById(id);
    }

    /**
     * 修改Specification
     *
     * @param specEntity
     */
    @Override
    public void update(SpecEntity specEntity) {
        Specification specification = specEntity.getSpecification();
        List<SpecificationOption> specEntitySpecificationOptionList = specEntity.getSpecificationOptionList();
        this.updateById(specification);
        QueryWrapper<SpecificationOption> specificationOptionQueryWrapper = new QueryWrapper<>();
        specificationOptionQueryWrapper.eq("spec_id",specification.getId());
        specificationOptionMapper.delete(specificationOptionQueryWrapper);
        if (CollectionUtils.isNotEmpty(specEntitySpecificationOptionList)){
            for (SpecificationOption specificationOption : specEntitySpecificationOptionList) {
                specificationOption.setSpecId(specification.getId());
                specificationOptionMapper.insert(specificationOption);
            }
        }
    }

    /**
     * 增加Specification
     *
     * @param specEntity
     */
    @Override
    public void add(SpecEntity specEntity) {
        Specification specification = specEntity.getSpecification();
        this.save(specification);
        List<SpecificationOption> specEntitySpecificationOptionList = specEntity.getSpecificationOptionList();
        if (specEntitySpecificationOptionList != null && specEntitySpecificationOptionList.size() > 0){
            for (SpecificationOption specificationOption : specEntitySpecificationOptionList) {
                specificationOption.setSpecId(specification.getId());
                specificationOptionMapper.insert(specificationOption);
            }
        }
    }

    /**
     * 根据ID查询Specification
     *
     * @param id
     * @return
     */
    @Override
    public SpecEntity findById(Long id) {
        Specification specification = this.getById(id);
        QueryWrapper<SpecificationOption> specificationOptionQueryWrapper = new QueryWrapper<>();
        specificationOptionQueryWrapper.eq("spec_id",id);
        List<SpecificationOption> specificationOptionList = specificationOptionMapper.selectList(specificationOptionQueryWrapper);
        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setSpecificationOptionList(specificationOptionList);
        return specEntity;
    }

    /**
     * 查询Specification全部数据
     *
     * @return
     */
    @Override
    public List<Specification> findAll() {
        return this.list(new QueryWrapper<Specification>());
    }
}
