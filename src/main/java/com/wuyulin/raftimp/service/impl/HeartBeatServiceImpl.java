package com.wuyulin.raftimp.service.impl;

import com.wuyulin.raftimp.model.Follower;
import com.wuyulin.raftimp.model.HeartBeatRequest;
import com.wuyulin.raftimp.model.Role;
import com.wuyulin.raftimp.service.IHeartBeatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatServiceImpl implements IHeartBeatService {

    @Value("${heartbeat.timeout.period}")
    private static Long heartbeatTimeoutPeriod;

    @Override
    public Object listen(HeartBeatRequest heartBeatRequest) throws Exception {
        // 收到心跳，将计时器重置
        if (heartbeatTimeoutPeriod == null || heartbeatTimeoutPeriod <= 0){
            throw new Exception("请配置心跳超时时间！");
        }
        if (Role.role.getClass() == Follower.class){
            // 未超时，续约
            Follower.renewTreat();
            System.out.println("HeartBeat续约成功！");
        }
        return true;
    }


}
