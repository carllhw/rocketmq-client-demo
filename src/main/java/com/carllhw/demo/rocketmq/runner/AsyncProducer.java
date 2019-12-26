package com.carllhw.demo.rocketmq.runner;

import java.util.concurrent.TimeUnit;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * async producer
 *
 * @author carllhw
 */
@Slf4j
public class AsyncProducer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Override
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducer");
        // 设置NameServer的地址
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        // 启动Producer实例
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        int messageCount = 100;
        // 根据消息数量实例化倒计时计算器
        final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TOPIC_TEST", "TagA", "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("{} success {}", index, sendResult.getMsgId());
                    countDownLatch.countDown();
                }

                @Override
                public void onException(Throwable e) {
                    log.error("{} error {}", index, e);
                    countDownLatch.countDown();
                }
            });
        }
        // 等待5s
        countDownLatch.await(5, TimeUnit.SECONDS);
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
