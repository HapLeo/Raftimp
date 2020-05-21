package com.wuyulin.raftimp.config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 当前节点状态
 */
public class NodeStatus {

    /**
     * 谁是LeaderNode
     */
    public static String leaderNode;

    /**
     * 当前节点的任期
     */
    public static AtomicInteger term = new AtomicInteger(1);

    public static String localUrl = "http://localhost:8080";
    public static Integer heartbeatTimeoutPeriod = 1000;
    /**
     * 投票接口
     */
    public static List<String> voteUrls = Arrays.asList("http://localhost:8080/vote/checkVoteTicket","http://localhost:8081/vote/checkVoteTicket","http://localhost:8082/vote/checkVoteTicket");
    /**
     * 心跳接口
     */
    public static List<String> heartBeatUrls = Arrays.asList("http://localhost:8080/heartBeat/listen","http://localhost:8081/heartBeat/listen","http://localhost:8082/heartBeat/listen");
}
