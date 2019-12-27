package com.carllhw.demo.rocketmq.runner;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * consumer in order
 * 顺序消息消费，带事务方式（应用可控制Offset什么时候提交）
 *
 * @author carllhw
 */
@Slf4j
@Component
public class ConsumerInOrder implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerInOrder");
        consumer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        consumer.subscribe("TOPIC_TEST", "TagA || TagC || TagD");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            Random random = new Random();

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                for (MessageExt msg : msgs) {
                    // 可以看到每个queue有唯一的consume线程来消费, 订单对每个queue(分区)有序
                    log.info("consumeThread={}, queueId={}, content:{}", Thread.currentThread().getName(),
                            msg.getQueueId(), new String(msg.getBody()));
                }
                try {
                    //模拟业务逻辑处理中...
                    TimeUnit.SECONDS.sleep(random.nextInt(10));
                } catch (Exception e) {
                    log.error("error", e);
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        log.info("Consumer Started.");
    }
}
