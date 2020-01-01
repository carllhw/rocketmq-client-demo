package com.carllhw.demo.rocketmq.runner;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * batch producer
 *
 * @author carllhw
 */
@Slf4j
@Component
public class BatchProducer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("BATCH_PRODUCE");
        // 设置NameServer的地址
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 启动Producer实例
        producer.start();
        String topic = "TOPIC_TEST";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes(
                StandardCharsets.UTF_8)));
        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes(
                StandardCharsets.UTF_8)));
        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes(
                StandardCharsets.UTF_8)));
        try {
            SendResult sendResult = producer.send(messages);
            log.info("{}", sendResult.getSendStatus());
        } catch (Exception e) {
            log.error("error", e);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
