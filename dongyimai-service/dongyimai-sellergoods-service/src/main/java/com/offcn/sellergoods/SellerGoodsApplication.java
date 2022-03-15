package com.offcn.sellergoods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.sellergoods.dao")
//@ComponentScan(basePackages = {"com.offcn.config"})
//卖家商品信息服务
public class SellerGoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellerGoodsApplication.class);
    }
}