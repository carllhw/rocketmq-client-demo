package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * consumer
 *
 * @author carllhw
 */
@Slf4j
@Component
public class Consumer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Consumer");
        // 设置NameServer的地址
        consumer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("TOPIC_TEST", "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            log.info("{} receive new messages: {}", Thread.currentThread().getName(), msgs);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        log.info("Consumer started");
    }
}
