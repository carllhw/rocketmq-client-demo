package com.carllhw.demo.rocketmq.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * rocketmq auto configuration
 *
 * @author carllhw
 */
@Configuration
@EnableConfigurationProperties(RocketmqProperties.class)
public class RocketmqAutoConfiguration {
}
