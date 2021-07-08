package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
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
import java.sql.Types;
import java.util.Date;
import java.util.List;
@Service
public class AccountServiceDaoImpl implements AccountServiceDao {
    private static final Logger LOGGER = LogManager.getLogger(AccountServiceDaoImpl.class);

    @Autowired
    private JdbcTemplate ds1JdbcTemplate;

    public JdbcTemplate getJdbcTemplate(){
        return this.ds1JdbcTemplate;
    }

    public List<UserEntity> getUserEntitys (Long id,String userName,String password,Integer flag, List<Error> errorList){
        List<UserEntity>  userEntityList = null;
        try{
            String sql = "select id,username,password,imageurl,createtime,updatetime,flag from sys_user where 1 = 1 ";
            if(null != id){
                sql = sql+" and id = "+id+"";
            }
            if(!StringUtils.isEmpty(userName)){
                sql = sql+" and username = '"+userName+"'";
            }
            if(!StringUtils.isEmpty(password)){
                sql = sql+" and password = '"+password+"'";
            }
            if(null != flag){
                sql = sql+" and flag = "+flag+"";
            }
            userEntityList = ds1JdbcTemplate.query(sql,new BeanPropertyRowMapper<>(UserEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询user表出错",e.toString()));
        }
        return  userEntityList;
    }

    public List<UserEntity> getUserEntitysForCheck(String keyWord,Integer flag, List<Error> errorList){
        List<UserEntity>  userEntityList = null;
        try{
            String sql = "select id,username,password,imageurl,createtime,updatetime,flag from sys_user where 1 = 1 ";
            if(!StringUtils.isEmpty(keyWord)){
                sql = sql+" and username like '%"+keyWord+"%'";
            }
            if(null != flag && !flag.equals(3)){
                sql = sql+" and flag = "+flag+"";
            }
            userEntityList = ds1JdbcTemplate.query(sql,new BeanPropertyRowMapper<>(UserEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询user表出错",e.toString()));
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
        String sql = "insert into sys_user(id,username,id_card,password,imageurl,createTime,flag) values(?,?,?,?,?,?,?)";
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(null != user.getId()) {
                        ps.setLong(1, user.getId());
                    }else{
                        ps.setNull(1, Types.BIGINT);
                    }
                    if(!StringUtils.isEmpty(user.getUserName())) {
                        ps.setString(2, user.getUserName());
                    }else{
                        ps.setNull(2,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(user.getIdCard())) {
                        ps.setString(3, user.getIdCard());
                    }else{
                        ps.setNull(3,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(user.getPassword())){
                        ps.setString(4, user.getPassword());
                    }else{
                        ps.setNull(4,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(user.getImageUrl())) {
                        ps.setString(5, user.getImageUrl());
                    }else{
                        ps.setNull(5,Types.VARCHAR);
                    }
                        ps.setTimestamp(6, new Timestamp(user.getCreateTime().getTime()));
                        ps.setInt(7,user.getFlag());
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入user表出错",e.toString()));
        }
        return result;
    }

    /**
     * 根据user表主键id更新用户信息
     * @param user
     * @param errorList
     * @return
     */
    @Override
    public  int update (UserEntity user, List<Error> errorList){
        int result=0;
        String sql = "update sys_user set username = ?,id_card = ?,password = ?,imageurl = ?,updatetime = ?,flag = ? where id = "+user.getId();
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, user.getUserName());
                    ps.setString(2, user.getIdCard());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, user.getImageUrl());
                    ps.setTimestamp(5,new Timestamp((new Date()).getTime()));
                    ps.setInt(6,user.getFlag());
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改user表出错",e.toString()));
        }
        return  result;
    }

    /**
     *修改用户的审核状态
     */
    @Override
    public  int updateForCheck (Long id,Integer  flag, List<Error> errorList){
        int result=0;
        String sql = "update sys_user set flag = ? where id = "+id;
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setInt(1,flag);
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改user表出错",e.toString()));
        }
        return  result;
    }
    /**
     * 根据user表主键id物理删除用户
     * @param id
     * @param errorList
     * @return
     */
    @Override
    public int deleteUser(Long id, List<Error> errorList){
        int result = 0;
        try {
            String sql = "delete from sys_user where  id = " + id;
            result = ds1JdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_user数据出错",e.toString()));
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
            result = ds1JdbcTemplate.queryForObject(sql,Long.class);
        }catch (Exception e){
            LOGGER.error(e.toString());
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"获取user_seq失败",e.toString()));
        }
        return result;
    }

    /**
     * 初始化user表序列
     */
    @Transactional
    @Override
    public void initSeq() {
        String SQL_init_user_seq="insert into sys_sequence set name='user_seq',current_value=1000000000";
        ds1JdbcTemplate.update(SQL_init_user_seq);
    }
}
