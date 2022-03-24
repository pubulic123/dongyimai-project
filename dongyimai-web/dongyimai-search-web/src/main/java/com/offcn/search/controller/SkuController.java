package com.offcn.search.controller;

import com.offcn.entity.Page;
import com.offcn.search.feign.SearchSkuFeign;
import com.offcn.search.pojo.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SearchSkuFeign searchSkuFeign;

    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map searchMap, Model model) {
        Map resultMap = searchSkuFeign.search(searchMap);
        model.addAttribute("result", resultMap);
        model.addAttribute("searchMap", searchMap);
        //保存请求条件路径
        String url = setUrl(searchMap);
        model.addAttribute("url", url);

        //将数据存入到分页对象中
        Page<SkuInfo> page = new Page(
                Long.valueOf(resultMap.get("total").toString()),
                Integer.valueOf(resultMap.get("pageNum").toString()),
                Integer.valueOf(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page", page);


        return "search";
    }


    //http://localhost:9101/search/list?keywords=手机&brand=海信&
    private String setUrl(Map<String, String> searchMap) {
        //设置父级请求路径
        String url = "/search/list";
        if (!CollectionUtils.isEmpty(searchMap)) {
            url += "?";
            for (String key : searchMap.keySet()) {
                //对分页 不进行保存
                if (key.equals("pageNum")) {
                    continue;
                }
                //排序 不进行保存
                if (key.equals("sortRule") || key.equals("sortField")) {
                    continue;
                }

                String value = searchMap.get(key);
                url += key + "=" + value + "&";
            }

            //去除最后的&
            if (url.lastIndexOf("&") != -1) {
                url.substring(0, url.lastIndexOf("&"));
            }
        }
        return url;
    }
}
