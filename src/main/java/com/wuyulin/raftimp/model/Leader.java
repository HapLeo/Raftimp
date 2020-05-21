package com.wuyulin.raftimp.model;

import com.alibaba.fastjson.JSON;
import com.wuyulin.raftimp.config.NodeStatus;
import com.wuyulin.raftimp.util.HttpClientTool;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

@Data
public class Leader extends Role {

    private Logger logger = LoggerFactory.getLogger(Leader.class);

    private static final Timer timer = new Timer();

    @Override
    public void run() {
        logger.info("以Leader身份运行...");
        renewTreat();
    }

    @Override
    public void renewTreat() {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (Role.role.getClass() != Leader.class) {
                    timer.cancel();
                    return;
                }

                // 发送Leader心跳
                if (Role.role instanceof Leader) {
                    sendHeartBeat();
                }
            }
        }, 0, NodeStatus.heartbeatTimeoutPeriod);
    }

    /**
     * 发送心跳
     */
    private void sendHeartBeat() {

        HeartBeatRequest heartBeatRequest = new HeartBeatRequest();
        heartBeatRequest.setTerm(NodeStatus.term.get());
        heartBeatRequest.setLeaderNode(NodeStatus.localUrl);
        heartBeatRequest.setData("心跳正常发送！--from:" + NodeStatus.localUrl);
        for (String heartBeatUrl : NodeStatus.heartBeatUrls) {
            if (heartBeatUrl.contains(NodeStatus.localUrl)){
                continue;
            }
            HttpClientTool.call(heartBeatUrl, JSON.toJSONString(heartBeatRequest));
        }
    }
}
