package com.carllhw.demo.rocketmq.runner;

/**
 * demo runner enum
 *
 * @author carllhw
 */
public enum DemoRunnerEnum {

    /**
     * SyncProducer
     */
    SyncProducer("SyncProducer", new SyncProducer()),

    AsyncProducer("AsyncProducer", new AsyncProducer()),

    OnewayProducer("OnewayProducer", new OnewayProducer()),

    Consumer("Consumer", new Consumer()),

    ProducerInOrder("ProducerInOrder", new ProducerInOrder()),

    ConsumerInOrder("ConsumerInOrder", new ConsumerInOrder()),

    ScheduledMessageConsumer("ScheduledMessageConsumer", new ScheduledMessageConsumer()),

    ScheduledMessageProducer("ScheduledMessageProducer", new ScheduledMessageProducer());

    private final String code;

    private DemoRunner demoRunner;

    DemoRunnerEnum(String code, DemoRunner demoRunner) {
        this.code = code;
        this.demoRunner = demoRunner;
    }

    public String getCode() {
        return code;
    }

    public DemoRunner getDemoRunner() {
        return demoRunner;
    }
}
