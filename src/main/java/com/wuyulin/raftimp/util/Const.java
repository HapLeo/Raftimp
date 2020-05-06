package com.wuyulin.raftimp.util;

import java.util.Arrays;
import java.util.List;

public interface Const {

    String url0 = "http://localhost:8080/voteLeader";
    String url1 = "http://localhost:8081/voteLeader";
    String url2 = "http://localhost:8082/voteLeader";

    /**
     * 服务器节点列表
     */
    List<String> serverList = Arrays.asList(url0,url1,url2);

}
