package com.sugon.iris.sugondata.jpa.impl.system;

import com.sugon.iris.sugondata.jpa.intf.system.AccountServiceDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.userEntities.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Service
public class AccountServiceDaoImpl implements AccountServiceDaoIntf {
    private static final Logger LOGGER = LogManager.getLogger(AccountServiceDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<UserEntity> getUserEntitys (Long id,String email,String password,Integer flag, List<Error> errorList){
        List<UserEntity>  userEntityList = null;
        try{
            String sql = "select id,email,password,imageurl,createtime,updatetime,flag from sys_user where 1 = 1 ";
            if(null != id){
                sql = sql+" and id = "+id+"";
            }
            if(!StringUtils.isEmpty(email)){
                sql = sql+" and email = '"+email+"'";
            }
            if(!StringUtils.isEmpty(password)){
                sql = sql+" and password = '"+password+"'";
            }
            if(null != flag){
                sql = sql+" and flag = "+flag+"";
            }
            userEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(UserEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表user失败",e);
            errorList.add(new Error("{SYS-02-005}","查询user表出错",e.toString()));
        }
        return  userEntityList;
    }
    /**
     * 插入用户自信息
     * @param user
     * @param errorList
     * @return
     */
    @Transactional
    @Override
    public int insertAccount(UserEntity user, List<Error> errorList){
        int result=0;
        String sql = "insert into sys_user(id,email,password,imageurl,createTime,flag) values(?,?,?,?,?,?)";
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(null != user.getId()) {
                        ps.setLong(1, user.getId());
                    }
                    if(!StringUtils.isEmpty(user.getEmail())) {
                        ps.setString(2, user.getEmail());
                    }
                    if(!StringUtils.isEmpty(user.getPassword())) {
                        ps.setString(3, user.getPassword());
                    }
                    ps.setString(4, user.getImageUrl());
                    ps.setTimestamp(5, new Timestamp(user.getCreateTime().getTime()));
                    ps.setInt(6,user.getFlag());
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error("{SYS-02-001}","插入user表出错",e.toString()));
        }
        return result;
    }
    /**
     * 根据user表主键id更新用户信息
     * @param user
     * @param errorList
     * @return
     */
    @Transactional
    @Override
    public  int update (UserEntity user, List<Error> errorList){
        int result=0;
        String sql = "update sys_user set email = ?,password = ?,imageurl = ?,updatetime = ?,flag = ? where id = "+user.getId();
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(user.getEmail())) {
                        ps.setString(1, user.getEmail());
                    }
                    if(!StringUtils.isEmpty(user.getPassword())) {
                        ps.setString(2, user.getPassword());
                    }
                    if(!StringUtils.isEmpty(user.getImageUrl())) {
                        ps.setString(3, user.getImageUrl());
                    }
                    ps.setTimestamp(4,new Timestamp((new Date()).getTime()));
                    ps.setInt(5,user.getFlag());
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error("{SYS-02-002}","修改user表出错",e.toString()));
        }
      return  result;
    }

    /**
     * 根据user表主键id物理删除用户
     * @param user
     * @param errorList
     * @return
     */
    @Transactional
    @Override
    public int deleteUser(UserEntity user, List<Error> errorList){
        int result = 0;
        try {
            String sql = "delete from sys_user where  id = " + user.getId();
            result = jdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error("{SYS-02-003}","删除部门数据库出错",e.toString()));
        }
        return result;
    }
    /**
     * 获取user表序列
     * @param errorList
     * @return
     */
    @Override
    public Long getUser_seq(List<Error> errorList){
        Long result = null;
        try {
            String sql = "select nextval('user_seq')";
            result = jdbcTemplate.queryForObject(sql,Long.class);
        }catch (Exception e){
            LOGGER.error(e.toString());
            errorList.add(new Error("{SYS-02-004}","获取user_seq失败",e.toString()));
        }
        return result;
    }

    /**
     * 初始化user表序列
     */
    @Transactional
    @Override
    public void initUserSeq() {
        String sql_init_user_seq="insert into sys_sequence set name='user_seq',current_value=1000000000";
        jdbcTemplate.update(sql_init_user_seq);
    }
}
