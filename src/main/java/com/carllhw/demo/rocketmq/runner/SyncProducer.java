package com.carllhw.demo.rocketmq.runner;

import java.nio.charset.StandardCharsets;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import com.carllhw.demo.rocketmq.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * sync producer runner
 *
 * @author carllhw
 */
@Slf4j
@Component
public class SyncProducer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("SYNC_PRODUCE");
        // 设置NameServer的地址
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < Constants.Digital.ONE_HUNDRED; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TOPIC_TEST", "TagA",
                    ("Hello RocketMQ " + i).getBytes(StandardCharsets.UTF_8));
            msg.putUserProperty("a", String.valueOf(i));
            // 发送消息到一个Broker
            SendResult sendResult = producer.send(msg);
            // 通过sendResult返回消息是否成功送达
            log.info("{}", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
