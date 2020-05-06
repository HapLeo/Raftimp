package com.wuyulin.raftimp.model;

import com.wuyulin.raftimp.config.RaftConfig;
import lombok.Data;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Follower的工作内容
 * 1. 监听来自Leader的心跳
 * 2. 监听不到心跳时参与选举
 * 3. 接收来自Leader的日志复制请求
 */
@Data
public class Follower extends Role {

    private static Timer timer;

    @Override
    public void run() throws Exception {
        System.out.println("以Follower身份开始运行...");
        renewTreat();
    }

    /**
     * 续约
     * 如果超时没有执行该方法，则角色会从Follower转换成CANDIDATE
     */
    public static void renewTreat() throws Exception {
        // 收到心跳，将计时器重置
        if (RaftConfig.heartbeatTimeoutPeriod <= 0){
            throw new Exception("请配置心跳超时时间！");
        }

        System.out.println("续约Follower身份...");
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // 接收Leader心跳超时，转换角色为Candidate
                Role.role = new Candidate();
                try {
                    role.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },RaftConfig.heartbeatTimeoutPeriod);
    }
}
