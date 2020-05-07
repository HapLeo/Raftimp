package com.wuyulin.raftimp.model;

import com.wuyulin.raftimp.config.RaftConfig;
import com.wuyulin.raftimp.util.Const;
import lombok.Data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

@Data
public class Candidate extends Role {

    private static Timer timer;

    @Override
    public void run() throws Exception {
        System.out.println("以Candidate身份开始运行...");

        // 给自己投票
        voteSelf();
        // 向其他节点发起选举请求
        for (String url : Const.serverList) {
            if (!Objects.equals(url, "localhost:" + RaftConfig.port)) {
                VoteResponse voteResponse = sendVoteForSelf(url, "vote me please.");
                System.out.println("发送投票请求到" + url);
                // 如果返回的任期比自己大，则将任期设置为大的那个，设置leader_url
                if (voteResponse.getTerm().compareTo(Role.term.get()) > 0){
                    Role.term.set(voteResponse.getTerm());
                    Leader.Leader_url = voteResponse.getLeader_url();
                    return; 
                }
                if (Objects.equals(voteResponse.getCode(),200)) {
                    int cnt = Role.liveNodeCnt.incrementAndGet();
                    if (cnt >= Const.serverList.size() / 2 + 1) {
                        Role.role = new Leader();
                        role.run();
                        break;
                    }
                }
            }
        }
        renewTreat();
    }

    /**
     * 为自己发起投票
     */
    private VoteResponse sendVoteForSelf(String url, String msg) {
        HttpClient.Builder builder = HttpClient.newBuilder();
        HttpClient httpClient = builder.version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NEVER)
                .executor(Executors.newFixedThreadPool(5))
                .build();
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder();
        HttpRequest request = reqBuilder.header("Content-Type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(2000))
                .POST(HttpRequest.BodyPublishers.ofString(msg))
                .build();
        VoteResponse voteResponse = new VoteResponse();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.uri() + "返回结果：" + response.body());
            voteResponse.setCode(500);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return voteResponse;
    }

    /**
     * 给自己投1票
     */
    private void voteSelf() {

        // 将活跃节点数加1，表示自己是活跃的，接受自己作为Leader
        Role.liveNodeCnt.incrementAndGet();
    }

    /**
     * 续约
     * 如果超时没有执行该方法，则会将任期加1再次发起投票
     */
    public static void renewTreat() throws Exception {
        // 收到心跳，将计时器重置
        if (RaftConfig.heartbeatTimeoutPeriod <= 0) {
            throw new Exception("请配置心跳超时时间！");
        }

        System.out.println("续约Candidate身份...");
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // 接收其他节点投票超时，任期加1，重新发送选票
                Role.role = new Candidate();
                int term = Role.term.incrementAndGet();
                System.out.println("任期变为：" + term);
                try {
                    role.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, RaftConfig.heartbeatTimeoutPeriod);
    }
}
