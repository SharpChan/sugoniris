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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
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

    public List<UserEntity> getUserEntitys (Long id,String userName,String password,String idCard,Integer flag,String policeno, List<Error> errorList){
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
            if(!StringUtils.isEmpty(idCard)){
                sql = sql+" and id_card = '"+idCard+"'";
            }
            if(null != flag){
                sql = sql+" and flag = "+flag+"";
            }
            if(!StringUtils.isEmpty(policeno)){
                sql = sql+" and policeno = '"+policeno+"'";
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
            String sql = "select id,username,password,imageurl,createtime,updatetime,flag,id_card as idCard,policeno from sys_user where 1 = 1 ";
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
    //@Transactional
    @Override
    public int insertAccount(UserEntity user, List<Error> errorList){
        int result=0;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into sys_user(username,id_card,password,imageurl,createTime,flag,policeNo) values(?,?,?,?,?,?,?)";
        try{
            ds1JdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps  = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    if(!StringUtils.isEmpty(user.getUserName())) {
                        ps.setString(1, user.getUserName());
                    }else{
                        ps.setNull(1,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(user.getIdCard())) {
                        ps.setString(2, user.getIdCard());
                    }else{
                        ps.setNull(2,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(user.getPassword())){
                        ps.setString(3, user.getPassword());
                    }else{
                        ps.setNull(3,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(user.getImageUrl())) {
                        ps.setString(4, user.getImageUrl());
                    }else{
                        ps.setNull(4,Types.VARCHAR);
                    }
                    ps.setTimestamp(5, new Timestamp(user.getCreateTime().getTime()));
                    ps.setInt(6,user.getFlag());
                    if(!StringUtils.isEmpty(user.getPoliceNo())){
                        ps.setString(7, user.getPoliceNo());
                    }else{
                        ps.setNull(7,Types.VARCHAR);
                    }
                    return ps;
                }
            }, keyHolder);
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"注册信息存在重复！",e.toString()));
        }
        user.setId(keyHolder.getKey().longValue());
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
     *修改用户密码
     */
    @Override
    public  int updateForPassword (Long id,String password, List<Error> errorList){
        int result=0;
        String sql = "update sys_user set password = ? where id = "+id;
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1,password);
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","修改密码失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改密码失败",e.toString()));
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
