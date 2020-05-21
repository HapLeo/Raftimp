package com.wuyulin.raftimp.service.impl;

import com.wuyulin.raftimp.model.*;
import com.wuyulin.raftimp.service.IHeartBeatService;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatServiceImpl implements IHeartBeatService {

    @Override
    public HeartBeatResponse listen(HeartBeatRequest heartBeatRequest) {
        // 收到心跳，将计时器重置
        Role.role = new Follower();
        Role.role.run();
        HeartBeatResponse heartBeatResponse = new HeartBeatResponse();
       // heartBeatResponse
        return heartBeatResponse;
    }


}
