package com.wuyulin.raftimp.service.impl;

import com.wuyulin.raftimp.RoleEnum;
import com.wuyulin.raftimp.StatusMachine;
import com.wuyulin.raftimp.model.HeartBeatRequest;
import com.wuyulin.raftimp.service.IHeartBeatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class HeartBeatServiceImpl implements IHeartBeatService {

    @Value("${heartbeat.timeout.period}")
    private Long timeoutPeriod;

    @Value("${server.port}")
    private String port;

    private Timer timer;

    @Override
    public Object listen(HeartBeatRequest heartBeatRequest) throws Exception {
        // 收到心跳，将计时器重置
        if (timeoutPeriod == null || timeoutPeriod <= 0){
            throw new Exception("请配置心跳超时时间！");
        }
        if (StatusMachine.role == RoleEnum.FOLLOWER){
            // 未超时，续约
            renewTreat();
            System.out.println("HeartBeat续约成功！");
        }
        return StatusMachine.role;
    }

    /**
     * 续约
     */
    private void renewTreat() {
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
             StatusMachine.role = RoleEnum.CANDIDATE;
             StatusMachine.heartBeatTimeOut = true;
            }
        },timeoutPeriod);
    }
}
