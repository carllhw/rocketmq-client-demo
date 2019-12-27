package com.carllhw.demo.rocketmq.runner;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
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

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> demoRunnerCodeList = args.getOptionValues(DEMO_RUNNER);
        if (CollectionUtils.isEmpty(demoRunnerCodeList)) {
            return;
        }
        String demoRunnerCode = demoRunnerCodeList.get(0);
        DemoRunner demoRunner = applicationContext.getBean(demoRunnerCode, DemoRunner.class);
        beforeRun();
        demoRunner.run();
    }

    private void beforeRun() {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
    }
}
