package com.wuyulin.raftimp.config;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 投票状态
 */
@Data
public class VoteStatus {

    /**
     * 任期
     */
    private Integer term;

    /**
     * 票数
     */
    private AtomicInteger voteCnt = new AtomicInteger(0);
}
