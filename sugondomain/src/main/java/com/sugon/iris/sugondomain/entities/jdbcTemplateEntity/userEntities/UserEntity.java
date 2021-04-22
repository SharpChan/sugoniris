package com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.userEntities;

import com.fasterxml.jackson.annotation.JsonFormat;
import  com.sugon.iris.sugondomain.beans.configBeans.Constants;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserEntity implements Serializable {

    private Long id;

    @Pattern(regexp = Constants.EMAIL_REGEX)
    private String email;

    @Size(min = 1, max = 50)
    private String password;

    @Size(max = 256)
    private String imageUrl;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 用户是否被锁定 true:被锁定；false：未被锁定
     */
    private boolean isLocked = false;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date createTime = new Date();

    //0:无效 1：有效
    private Integer flag = 1;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss.SSSSSS",timezone="GMT+8")
    private Date updateTime;
}
