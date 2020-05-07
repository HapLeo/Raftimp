package com.wuyulin.raftimp.model;

import lombok.Data;

/**
 * 投票选举请求
 */
@Data
public class VoteRequest {

    /**
     * 投票发起方url
     */
    private String fromUrl;

    /**
     * 投票发起方任期
     */
    private Integer term;

}
