package com.sugon.iris.sugondomain.beans.jmBeans;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class JM_t_system_userBean {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色id
     */
    private Integer userRoleId;

    /**
     * 部门编号
     */
    private String deptId;

    /**
     * 证件号
     */
    private String cardNo;

    /**
     * 创建时间
     */
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /**
     * 账号名称
     */
    private String accountNo;


    /**
     * 密码
     */
    private String passWord;



    private String modelExecuteType= "0";

    //用户有效期
    private Date validity_time;

}
