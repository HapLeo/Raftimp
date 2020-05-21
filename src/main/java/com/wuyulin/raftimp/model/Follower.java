package com.wuyulin.raftimp.model;

import com.wuyulin.raftimp.config.NodeStatus;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Logger logger = LoggerFactory.getLogger(Follower.class);

    private static Timer timer;

    @Override
    public void run(){
        logger.info("以Follower身份开始运行...");
        renewTreat();
    }

    /**
     * 续约
     * 如果超时没有执行该方法，则角色会从Follower转换成CANDIDATE
     */
    @Override
    public void renewTreat(){
        // 收到心跳，将计时器重置
        logger.info("续约Follower身份...");
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                logger.info("接收心跳超时，转换角色:Follower -> Candidate.");
                // 接收Leader心跳超时，转换角色为Candidate
                Role.role = new Candidate();
                role.run();
            }
        }, NodeStatus.heartbeatTimeoutPeriod);
    }
}
