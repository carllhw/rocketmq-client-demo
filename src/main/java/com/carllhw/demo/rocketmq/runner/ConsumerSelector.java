package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * consumer selector
 *
 * @author carllhw
 */
@Slf4j
@Component
public class ConsumerSelector implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Consumer");
        consumer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 只有订阅的消息有这个属性a, a >=0 and a <= 3
        consumer.subscribe("TOPIC_TEST", MessageSelector.bySql("a between 0 and 3"));
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            log.info("{} receive new messages: {}", Thread.currentThread().getName(), msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
