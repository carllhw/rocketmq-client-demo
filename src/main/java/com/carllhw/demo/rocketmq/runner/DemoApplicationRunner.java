package com.carllhw.demo.rocketmq.runner;

import java.util.List;

import com.carllhw.demo.rocketmq.autoconfigure.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * demo application runner
 *
 * @author carllhw
 */
@Slf4j
@Profile("!test")
@Component
public class DemoApplicationRunner implements ApplicationRunner {

    private static final String DEMO_RUNNER = "demoRunner";

    private RocketmqProperties rocketmqProperties;

    @Autowired
    public void setRocketmqProperties(RocketmqProperties rocketmqProperties) {
        this.rocketmqProperties = rocketmqProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> demoRunnerCodeList = args.getOptionValues(DEMO_RUNNER);
        if (CollectionUtils.isEmpty(demoRunnerCodeList)) {
            return;
        }
        String demoRunnerCode = demoRunnerCodeList.get(0);
        DemoRunnerEnum demoRunnerEnum = DemoRunnerEnum.valueOf(demoRunnerCode);
        DemoRunner runner = demoRunnerEnum.getDemoRunner();
        runner.setRocketmqProperties(rocketmqProperties);
        runner.run();
    }
}
