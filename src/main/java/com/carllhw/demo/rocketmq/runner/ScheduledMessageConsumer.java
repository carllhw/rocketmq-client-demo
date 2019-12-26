package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * scheduled message consumer
 *
 * @author carllhw
 */
@Slf4j
public class ScheduledMessageConsumer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Override
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ScheduledMessageConsumer");
        // 设置NameServer的地址
        consumer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 订阅Topics
        consumer.subscribe("TOPIC_TEST", "*");
        // 注册消息监听者
        consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
            for (MessageExt message : messages) {
                // Print approximate delay time period
                log.info("Receive message[msgId={}] {}ms later", message.getMsgId(),
                        (System.currentTimeMillis() - message.getBornTimestamp()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者
        consumer.start();
    }
}
