package com.offcn.sellergoods.controller;

import com.offcn.entity.PageResult;
import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.sellergoods.pojo.Brand;
import com.offcn.sellergoods.service.BrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    /***
     * 查询全部数据
     * @return
     */
    @GetMapping //不写value值默认调用根路径
    public Result<List<Brand>> findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK,"查询成功",brandList) ;
    }
    /***
     * 根据ID查询Brand数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable Long id){
        //调用BrandService实现根据主键查询Brand
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true,StatusCode.OK,"查询成功",brand);
    }

    /***
     * 新增Brand数据
     * @pa am brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   Brand brand){
        //调用BrandService实现添加Brand
        try {
            brandService.add(brand);
            return new Result(true,StatusCode.OK,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR,"添加失败");
        }
    }

    /***
     * 修改Brand数据
     * @param brand
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Brand brand,@PathVariable Long id){
        try {
            //设置主键值
            brand.setId(id);
            //调用BrandService实现修改Brand
            brandService.update(brand);
            return new Result(true,StatusCode.OK,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR,"修改失败");
        }
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        try {
            //调用BrandService实现根据主键删除
            brandService.delete(id);
            return new Result(true,StatusCode.OK,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR,"删除失败");
        }
    }

    /***
     * 多条件搜索品牌数据
     * @param brand
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Brand>> findList(@RequestBody(required = false) Brand brand){
        List<Brand> list = brandService.findList(brand);
        return new Result<List<Brand>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * Brand分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageResult<Brand>> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用BrandService实现分页查询Brand
        PageResult<Brand> pageResult = brandService.findPage(page, size);
        return new Result<PageResult<Brand>>(true,StatusCode.OK,"查询成功",pageResult);
    }

    /***
     * 分页搜索实现
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageResult> findPage(@RequestBody(required = false) Brand brand, @PathVariable  int page, @PathVariable  int size){
        //执行搜索
        PageResult<Brand> pageResult = brandService.findPage(brand, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
    @ApiOperation(value = "查询品牌下拉列表",notes = "查询品牌下拉列表",tags = {"BrandController"})
    @GetMapping("/selectOptions")
    public ResponseEntity<List<Map>> selectOptions(){
        return ResponseEntity.ok(brandService.selectOptions());
    }
}