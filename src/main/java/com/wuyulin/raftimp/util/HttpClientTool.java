package com.wuyulin.raftimp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;

public class HttpClientTool {

    private static Logger logger = LoggerFactory.getLogger(HttpClientTool.class);

    public static String call(String url,String requestBody){

        java.net.http.HttpClient.Builder builder = java.net.http.HttpClient.newBuilder();
        java.net.http.HttpClient httpClient = builder.version(java.net.http.HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(java.net.http.HttpClient.Redirect.NEVER)
                .executor(Executors.newFixedThreadPool(5))
                .build();
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder();
        HttpRequest request = reqBuilder.header("Content-Type", "application/json")
                .version(java.net.http.HttpClient.Version.HTTP_1_1)
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(2000))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("返回结果：" + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            //logger.error(e.getMessage());
        }
        return null;
    }
}
