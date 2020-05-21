package com.wuyulin.raftimp.service;

import com.wuyulin.raftimp.model.HeartBeatRequest;
import com.wuyulin.raftimp.model.HeartBeatResponse;

public interface IHeartBeatService {


    /**
     * 监听Leader发来的心跳
     * 每次续期300ms
     * 持续300ms未接收到心跳则转换角色发起新一轮任期投票
     *
     * @param heartBeatRequest
     * @return
     */
    HeartBeatResponse listen(HeartBeatRequest heartBeatRequest) throws Exception;
}
