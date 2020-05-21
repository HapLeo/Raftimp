package com.wuyulin.raftimp.model;

import com.alibaba.fastjson.JSON;
import com.wuyulin.raftimp.config.NodeStatus;
import com.wuyulin.raftimp.config.VoteStatus;
import com.wuyulin.raftimp.util.HttpClientTool;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Data
public class Candidate extends Role {

    private Logger logger = LoggerFactory.getLogger(Candidate.class);

    private static final Timer timer = new Timer();

    @Override
    public void run() {
        logger.info("以Candidate身份开始运行...");
        renewTreat();
    }


    /**
     * 续约
     * 如果超时没有执行该方法，则会将任期加1再次发起投票
     */
    @Override
    public void renewTreat() {
        // 收到心跳，将计时器重置
        logger.info("续约Candidate身份...");

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (Role.role instanceof Leader) {
                    timer.cancel();
                    return;
                }
                // 接收其他节点投票超时，任期加1，重新发送选票
                Role.role = new Candidate();
                int term = NodeStatus.term.incrementAndGet();
                logger.info("任期变为：" + term);
                VoteStatus voteStatus = loopGetVote();

                // 过半则晋升到Leader
                logger.info("本次通过的票数为：" + voteStatus.getVoteCnt() + "，总票数为：" + NodeStatus.voteUrls.size());
                if (voteStatus.getVoteCnt().get() >= NodeStatus.voteUrls.size() / 2 + 1) {
                    logger.info("通过选举，晋升为Leader,任期为：" + NodeStatus.term);
                    Role.role = new Leader();
                    role.run();
                } else {
                    renewTreat();
                }
            }

        }, 0, NodeStatus.heartbeatTimeoutPeriod);
    }


    /**
     * 循环其他节点，发送选举请求
     */
    private VoteStatus loopGetVote() {

        // 发起投票
        // # 初始化当前任期的投票状态
        VoteStatus voteStatus = new VoteStatus();
        // # 将自己的任期自增
        voteStatus.setTerm(NodeStatus.term.get());
        voteStatus.getVoteCnt().incrementAndGet();

        // 向其他节点发起选举请求
        for (String url : NodeStatus.voteUrls) {

            // 如果是自己，不要发送请求
            if (url.contains(NodeStatus.localUrl)) {
                continue;
            }
            VoteRequest voteRequest = new VoteRequest();
            voteRequest.setFromUrl(NodeStatus.localUrl);
            voteRequest.setTerm(NodeStatus.term.get());
            VoteResponse voteResponse = sendVote(url, voteRequest);
            if (voteResponse == null) {
                logger.error("节点：" + url + "未响应此次投票。");
                continue;
            }
            // 如果返回的任期比自己大，则将任期设置为大的那个，设置leader_url
            Integer remoteTerm = voteResponse.getTerm();
            int currentTerm = NodeStatus.term.get();
            if (remoteTerm == null) {
                logger.error("节点：" + url + "未返回正确的任期！");
                continue;
            }
            if (remoteTerm > currentTerm) {
                NodeStatus.term.set(voteResponse.getTerm());
                NodeStatus.leaderNode = voteResponse.getLeader_url();
            }
            if (Objects.equals(voteResponse.getCode(), 200)) {
                // 投票成功，票数累加
                voteStatus.getVoteCnt().incrementAndGet();
            }
        }
        return voteStatus;
    }


    /**
     * 为自己发起投票
     */
    private VoteResponse sendVote(String url, VoteRequest voteRequest) {

        VoteResponse voteResponse = null;

        String requestBody = JSON.toJSONString(voteRequest);
        String respObj = HttpClientTool.call(url, requestBody);
        if (respObj != null) {
            voteResponse = JSON.parseObject(respObj, VoteResponse.class);
        }
        return voteResponse;
    }

}
