package com.wuyulin.raftimp.model;

import com.wuyulin.raftimp.util.Const;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;

@Data
public class Candidate extends Role {

    @Value("${server.port}")
    private static String port;

    @Override
    public void run() {
        System.out.println("以Candidate身份开始运行...");

        // 给自己投票
        voteSelf();
        // 向其他节点发起选举请求
        Const.serverList.forEach(server -> {
            if (!Objects.equals(server, "localhost:" + port)) {
                HttpClient.Builder builder = HttpClient.newBuilder();
                HttpClient httpClient = builder.version(HttpClient.Version.HTTP_1_1)
                        .connectTimeout(Duration.ofMillis(5000))
                        .followRedirects(HttpClient.Redirect.NEVER)
                        .executor(Executors.newFixedThreadPool(5))
                        .build();
                HttpRequest.Builder reqBuilder = HttpRequest.newBuilder();
                HttpRequest request = reqBuilder.header("Content-Type", "application/json")
                        .version(HttpClient.Version.HTTP_1_1)
                        .uri(URI.create(server))
                        .timeout(Duration.ofMillis(2000))
                        .POST(HttpRequest.BodyPublishers.ofString("please vote for me..."))
                        .build();
                try {
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response.uri() + "返回结果：" +  response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 给自己投1票
     */
    private void voteSelf() {
    }
}
