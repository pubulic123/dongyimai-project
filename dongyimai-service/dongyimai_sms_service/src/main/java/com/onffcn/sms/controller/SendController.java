package com.onffcn.sms.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RequestMapping("/")
    public void send() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", "186XXXX9365");
        map.put("code", "9999");
        rabbitTemplate.convertAndSend("dongyimai.sms.queue", map);
    }
}