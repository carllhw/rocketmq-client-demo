package com.carllhw.demo.rocketmq.runner;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * demo application runner
 *
 * @author carllhw
 */
@Slf4j
@Component
public class DemoApplicationRunner implements ApplicationRunner {

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DemoRunner runner = new Consumer(rocketmqProperties);
        runner.run();
    }
}
