package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.UserGroupDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserGroupDetailEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.UserGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository
public class UserGroupDaoImpl implements UserGroupDao {

    private static final Logger LOGGER = LogManager.getLogger(UserGroupDaoImpl.class);

    @Autowired
    private JdbcTemplate ds1JdbcTemplate;

    /**
     * 通过该人员找出和该人员同一组的所有人员id
     * @param userId
     * @return
     */
    @Override
    public List<Long> getGroupUserIdList(Long userId,List<Error> errorList) {
        List<Long> userIdList = null;
        try{
            String sql = "select distinct user_id from sys_user_group_detail where user_group_id in " +
                    "(select user_group_id from sys_user_group_detail where user_id="+userId+")";
            userIdList = ds1JdbcTemplate.queryForList(sql, Long.class);
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_user_group_detail失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user_group_detail出错",e.toString()));
        }
        return  userIdList;
    }

    @Override
    public List<UserGroupEntity> getUserGroupEntitys(List<Error> errorList) {
        List<UserGroupEntity>  userGroupEntityList = null;
        try{
            String sql = "select id,group_name,description,create_time,update_time,user_id ,(select count(*) from sys_user_group_detail b where a.id = b.user_group_id) as count from sys_user_group a ";
            userGroupEntityList = ds1JdbcTemplate.query(sql,new BeanPropertyRowMapper<>(UserGroupEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_user_group失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user_group出错",e.toString()));
        }
        return  userGroupEntityList;
    }

    @Override
    public UserGroupEntity getUserGroupEntityById(Long id, List<Error> errorList) {
        UserGroupEntity userGroupEntity = null;
        if(null == id){
            return  null;
        }
        try{
            String sql = "select id,group_name,description,create_time,update_time,user_id from sys_user_group  where id="+id;
            userGroupEntity = ds1JdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(UserGroupEntity.class));
        }catch (Exception e){
            LOGGER.info("{}-{}","查询表sys_user_group失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user_group出错",e.toString()));
        }

        return userGroupEntity;
    }

    @Override
    public Integer deleteUserGroupEntity(Long id, List<Error> errorList) {
        int result = 0;
        try {
            String sql = "delete from sys_user_group where  id = " + id;
            result = ds1JdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_user_group数据出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer updateUserGroupEntity(UserGroupEntity userGroupEntitySql, List<Error> errorList) {
        int result=0;
        String sql = "update sys_user_group set group_name = ?,description = ?,update_time = ? where id = "+userGroupEntitySql.getId();
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(userGroupEntitySql.getGroupName())) {
                        ps.setString(1, userGroupEntitySql.getGroupName());
                    }else{
                        ps.setString(1, null);
                    }
                    if(!StringUtils.isEmpty(userGroupEntitySql.getDescription())) {
                        ps.setString(2, userGroupEntitySql.getDescription());
                    }else{
                        ps.setString(2,null);
                    }
                    ps.setTimestamp(3,new Timestamp((new Date()).getTime()));
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","修改表sys_user_group失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改sys_user_group表出错",e.toString()));
        }
        return  result;
    }

    @Override
    public Integer insertUserGroupEntity(UserGroupEntity userGroupEntitySql, List<Error> errorList) {
        int result=0;
        String sql = "insert into sys_user_group(group_name,description,create_time,user_id) values(?,?,?,?)";
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(userGroupEntitySql.getGroupName())) {
                        ps.setString(1,userGroupEntitySql.getGroupName());
                    }else{
                        ps.setString(1,"");
                    }
                    if(!StringUtils.isEmpty(userGroupEntitySql.getDescription())) {
                        ps.setString(2, userGroupEntitySql.getDescription());
                    }else{
                        ps.setString(2,"");
                    }
                    ps.setTimestamp(3, new Timestamp(userGroupEntitySql.getCreateTime().getTime()));

                    ps.setLong(4,userGroupEntitySql.getUserId());

                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入user表出错",e.toString()));
        }
        return result;
    }

    @Override
    public List<UserEntity> getUserEntityList(Long groupId, List<Error> errorList) {
        List<UserEntity>  userEntityList = null;
        try{
            String sql = "select * from sys_user a " +
                                "where " +
                                "not exists" +
                                "         (select user_id from sys_user_group_detail b" +
                                "                          where    a.id = b.user_id" +
                                "                                and user_group_id= "+groupId+")";
            userEntityList = ds1JdbcTemplate.query(sql,new BeanPropertyRowMapper<>(UserEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user出错",e.toString()));
        }
        return  userEntityList;
    }

    @Override
    public List<UserEntity> getGroupUserEntityList(Long groupId, List<Error> errorList) {
        List<UserEntity>  userEntityList = null;
        try{
            String sql = "select * from sys_user a " +
                    "where " +
                    " exists" +
                    "         (select user_id from sys_user_group_detail b" +
                    "                          where    a.id = b.user_id" +
                    "                                and user_group_id= "+groupId+")";
            userEntityList = ds1JdbcTemplate.query(sql,new BeanPropertyRowMapper<>(UserEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_user失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表sys_user出错",e.toString()));
        }
        return  userEntityList;
    }

    @Override
    public Integer insertUserGroupDetailEntity(UserGroupDetailEntity userGroupDetailEntitySql, List<Error> errorList) {
        int result=0;
        String sql = "insert into sys_user_group_detail(user_id,user_group_id,create_time,create_user_id) values(?,?,?,?)";
        try{
            result=ds1JdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(null != userGroupDetailEntitySql.getUserId()) {
                        ps.setLong(1, userGroupDetailEntitySql.getUserId());
                    }else{
                        ps.setNull(1, Types.BIGINT);
                    }
                    if(null != userGroupDetailEntitySql.getUserGroupId()) {
                        ps.setLong(2, userGroupDetailEntitySql.getUserGroupId());
                    }else{
                        ps.setNull(2, Types.BIGINT);
                    }
                        ps.setTimestamp(3, new Timestamp(userGroupDetailEntitySql.getCreateTime().getTime()));
                    if(null != userGroupDetailEntitySql.getCreateUserId()) {
                        ps.setLong(4, userGroupDetailEntitySql.getCreateUserId());
                    }else{
                        ps.setNull(4, Types.BIGINT);
                    }
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表sys_user_group_detail失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入sys_user_group_detail表出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer deleteUserGroupDetailEntity(UserGroupDetailEntity userGroupDetailEntitySql, List<Error> errorList) {
        int result = 0;
        try {
            String sql = "delete from sys_user_group_detail where  1=1 ";
            if(null != userGroupDetailEntitySql.getUserId()){
                sql += " and user_id = " + userGroupDetailEntitySql.getUserId();
            }
            if(null != userGroupDetailEntitySql.getUserGroupId()){
                sql += " and  user_group_id = "+userGroupDetailEntitySql.getUserGroupId();
            }
            result = ds1JdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_user_group_detail数据出错",e.toString()));
        }
        return result;
    }

}
