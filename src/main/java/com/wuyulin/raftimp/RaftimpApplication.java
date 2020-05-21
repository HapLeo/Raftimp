package com.wuyulin.raftimp;

import com.wuyulin.raftimp.model.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RaftimpApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaftimpApplication.class, args);
        // 运行默认角色
        Role.role.run();
    }

}
