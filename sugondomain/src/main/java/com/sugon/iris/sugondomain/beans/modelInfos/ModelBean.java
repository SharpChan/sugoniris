package com.sugon.iris.sugondomain.beans.modelInfos;

import lombok.Data;

@Data
public class ModelBean {
    /**
     *模型id
     */
    private String modelId;

    /**
     *创建者（警号）
     */
    private String creator;

    /**
     *这个模型的所有共有和私有用户
     */
    private UsersBean usersBean;

    /**
     * 对于该民警该模型是共有还是私有模型
     */
    private String modelType;

    /**
     * 模型的sql语句
     */
    private String modelSql;

    /**
     * 模型执行状态
     */
    private String status;

    /**
     * 模型结果表编号
     */
    private String resultTableId;
}
