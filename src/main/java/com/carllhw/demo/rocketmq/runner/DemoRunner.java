package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;

/**
 * demo runner
 *
 * @author carllhw
 */
public interface DemoRunner {

    /**
     * 设置 rocketmq 属性
     *
     * @param rocketmqProperties rocketmq 属性
     */
    void setRocketmqProperties(RocketmqProperties rocketmqProperties);

    /**
     * run
     *
     * @throws Exception exception
     */
    void run() throws Exception;
}
