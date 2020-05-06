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

    public abstract void run() throws Exception;
}
