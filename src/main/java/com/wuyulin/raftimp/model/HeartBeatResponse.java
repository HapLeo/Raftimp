package com.wuyulin.raftimp.model;

import lombok.Data;

@Data
public class HeartBeatResponse implements HeartBeat {

    private Integer term;

    private String leaderNode;

    private String data; 
}
