package com.carllhw.demo.rocketmq.runner;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import com.carllhw.demo.rocketmq.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * transaction producer
 *
 * @author carllhw
 */
@Slf4j
@Component
public class TransactionProducer implements DemoRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run() throws Exception {
        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("TRANSACTION_PRODUCER");
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), r -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < Constants.Digital.TEN; i++) {
            try {
                Message msg = new Message("TOPIC_TEST", tags[i % tags.length], "KEY" + i,
                        ("Hello RocketMQ " + i).getBytes(StandardCharsets.UTF_8));
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                log.info("{}", sendResult);
                Thread.sleep(10);
            } catch (MQClientException e) {
                log.error("error", e);
            }
        }
        for (int i = 0; i < Constants.Digital.ONE_HUNDRED_THOUSAND; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }

    /**
     * transaction listener impl
     */
    public static class TransactionListenerImpl implements TransactionListener {

        private AtomicInteger transactionIndex = new AtomicInteger(0);
        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            int value = transactionIndex.getAndIncrement();
            int status = value % 3;
            localTrans.put(msg.getTransactionId(), status);
            return LocalTransactionState.UNKNOW;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTrans.get(msg.getTransactionId());
            if (status == null) {
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            switch (status) {
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                case 0:
                default:
                    return LocalTransactionState.UNKNOW;
            }
        }
    }
}
