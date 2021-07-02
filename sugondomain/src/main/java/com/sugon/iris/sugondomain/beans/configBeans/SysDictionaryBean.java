package com.sugon.iris.sugondomain.beans.configBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class SysDictionaryBean {

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 字典组
     */
    private String dicGroup;

    /**
     * 字典值
     */
    private String value;

    /**
     * 显示内容
     */
    private String dicShow;

    /**
     * 备注
     */
    private String comment;

    /**
     * 用户编号
     */
    private Long userId;

    //创建时间
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    //创建时间
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();
}
