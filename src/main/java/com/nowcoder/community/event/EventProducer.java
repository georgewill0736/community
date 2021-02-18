package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author 杜俊宏
 * Date on 2021/2/17 22:10
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    //处理事件(本质为发送消息)
    public void fileEvent(Event event) {
        // 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(),JSONObject.toJSONString(event));

    }



}
