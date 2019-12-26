package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * scheduled message producer
 *
 * @author carllhw
 */
@Slf4j
public class ScheduledMessageProducer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Override
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化一个生产者来产生延时消息
        DefaultMQProducer producer = new DefaultMQProducer("ScheduledMessageProducer");
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 启动生产者
        producer.start();
        int totalMessagesToSend = 100;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TOPIC_TEST", ("Hello scheduled message " + i).getBytes());
            message.setDelayTimeLevel(3);
            // 发送消息
            producer.send(message);
        }
        // 关闭生产者
        producer.shutdown();
    }
}
