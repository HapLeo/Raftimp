package com.wuyulin.raftimp.model;

import lombok.Data;

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
     * 当前角色主要任务
     * @throws Exception
     */
    public abstract void run();

    /**
     * 续约当前角色
     */
    public abstract void renewTreat();
}
