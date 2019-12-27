package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * oneway runner
 *
 * @author carllhw
 */
@Component
public class OnewayProducer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("OnewayProducer");
        // 设置NameServer的地址
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TOPIC_TEST", "TagA",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
