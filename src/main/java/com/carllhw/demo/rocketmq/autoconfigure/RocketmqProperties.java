package com.carllhw.demo.rocketmq.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rocketmq properties
 *
 * @author carllhw
 */
@Data
@ConfigurationProperties(prefix = RocketmqProperties.PREFIX)
public class RocketmqProperties {

    public static final String PREFIX = "rocketmq";

    private String namesrvAddr;
}
