package com.wuyulin.raftimp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RaftConfig {

    public static int heartbeatTimeoutPeriod = 100;

    @Value("${server.port}")
    public static String port;

}
