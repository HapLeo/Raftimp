package com.wuyulin.raftimp.model;

import lombok.Data;

/**
 * 投票响应
 */
@Data
public class VoteResponse {

    /**
     * 该响应来自于哪个url
     */
    private String fromUrl;

    /**
     * leader_url
     */
    private String leader_url;

    /**
     * 相应代码
     * 200:接受此选举
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 当前节点任期
     */
    private Integer term;
}
