package com.wuyulin.raftimp.util;

import java.util.Arrays;
import java.util.List;

public class Const {

    public static String url0 = "http://localhost:8080/vote/checkVoteTicket";
    public static String url1 = "http://localhost:8081/vote/checkVoteTicket";
    public static String url2 = "http://localhost:8082/vote/checkVoteTicket";

    /**
     * 服务器节点列表
     */
    public static List<String> serverList = Arrays.asList(url0,url1,url2);

}
