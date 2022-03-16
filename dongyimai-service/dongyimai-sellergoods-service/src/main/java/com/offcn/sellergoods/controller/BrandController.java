package com.offcn.sellergoods.controller;

import com.offcn.entity.PageResult;
import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.sellergoods.pojo.Brand;
import com.offcn.sellergoods.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
@CrossOrigin
@Api(tags = "BrandController")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> list = brandService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 根据id查询brand
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable int id) {
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询成功", brand);
    }

    /**
     * 根据请求体中的brand实体类添加
     *
     * @param brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 路径传入id，请求体传入brand实体，根据id修改brand
     *
     * @param brand
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result update(@RequestBody Brand brand, @PathVariable Long id) {
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * Url传入id删除数据库对应实体类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        brandService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 根据brand参数查询列表
     *
     * @param brand
     * @return
     */
    @PostMapping("/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 无条件分页查询，URL传入page，size
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<PageResult<Brand>> findPage(@PathVariable int page, @PathVariable int size) {
        PageResult<Brand> pageResult = brandService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 分页条件查询
     *
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findPage(@RequestBody(required = false) Brand brand, @PathVariable int page, @PathVariable int size) {
        PageResult<Brand> pageResult = brandService.findPage(brand, page, size);
        return new Result<>(true, StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "Brand下拉列表查询", notes = "Brand下拉列表查询详情", tags = "BrandController")
    @GetMapping("/selectOptions")
    public Result<List<Map<String, String>>> selectOptions() {
        List<Map<String, String>> mapList = brandService.selectOptions();
        return new Result<>(true, StatusCode.OK, "查询成功", mapList);
    }
}
