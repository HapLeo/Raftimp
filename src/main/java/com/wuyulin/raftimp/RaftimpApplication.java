package com.wuyulin.raftimp;

import com.wuyulin.raftimp.model.Role;
import com.wuyulin.raftimp.service.IHeartBeatService;
import com.wuyulin.raftimp.service.impl.HeartBeatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RaftimpApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(RaftimpApplication.class, args);

        // 运行默认角色（Follower）的任务
        Role.role.run();
    }
}
