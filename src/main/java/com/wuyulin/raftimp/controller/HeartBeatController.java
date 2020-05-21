package com.wuyulin.raftimp.controller;

import com.wuyulin.raftimp.model.HeartBeatRequest;
import com.wuyulin.raftimp.model.HeartBeatResponse;
import com.wuyulin.raftimp.service.IHeartBeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartBeat")
public class HeartBeatController {

    @Autowired
    private IHeartBeatService heartBeatService;


    @RequestMapping("/listen")
    public HeartBeatResponse listen(HeartBeatRequest heartBeatRequest) throws Exception {

        return heartBeatService.listen(heartBeatRequest);
    }
}
