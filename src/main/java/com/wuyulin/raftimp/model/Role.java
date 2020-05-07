package com.wuyulin.raftimp.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 角色
 */
@Data
public abstract class Role {

    /**
     * 当前角色
     */
    public static Role role = new Follower();

    /**
     * 任期
     */
    public static AtomicInteger term = new AtomicInteger(1);

    /**
     * 活跃节点数
     */
    public static AtomicInteger liveNodeCnt = new AtomicInteger(1);

    /**
     * 当前角色主要任务
     * @throws Exception
     */
    public abstract void run() throws Exception;
}
