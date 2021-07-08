package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RoleServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RoleEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;

@Repository
public class RoleServiceDaoImpl implements RoleServiceDao {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<RoleEntity> getRoles(RoleEntity roleEntity, List<Error> errorList) {

        List<RoleEntity>  roleEntityList = null;
        String sql = "SELECT * FROM sys_role  where 1=1";
        if(roleEntity != null ){
            if(null != roleEntity.getId()){
                sql += " and id = "+roleEntity.getId();
            }
            if(StringUtils.isNotEmpty(roleEntity.getRoleName())){
                sql += " and role_name = "+roleEntity.getRoleName();
            }
        }
        try{
            roleEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(RoleEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_role失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询sys_role表出错",e.toString()));
        }

        return  roleEntityList;
    }

    @Override
    public Integer saveRole(RoleEntity roleEntity, List<Error> errorList) {
        int result=0;
        if(null == roleEntity){
            return result;
        }
        String sql = "insert into sys_role(role_name,description,create_user_id,create_time) " +
                "values(?,?,?,?)";

        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(StringUtils.isNotEmpty(roleEntity.getRoleName())) {
                        ps.setString(1, roleEntity.getRoleName());
                    }else{
                        ps.setNull(1, Types.VARCHAR);
                    }
                    if(StringUtils.isNotEmpty(roleEntity.getDescription())) {
                        ps.setString(2, roleEntity.getDescription());
                    }else{
                        ps.setNull(2,Types.VARCHAR);
                    }
                    if(null != roleEntity.getCreate_user_id()) {
                        ps.setLong(3, roleEntity.getCreate_user_id());
                    }else{
                        ps.setNull(3,Types.BIGINT);
                    }
                    ps.setTimestamp(4, new Timestamp(roleEntity.getCreateTime().getTime()));
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表sys_role失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入sys_role表出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer updateRole(RoleEntity roleEntity, List<Error> errorList) {
        int result=0;
        if(null == roleEntity){
            return result;
        }
        String sql = "update sys_role set role_name = ?,description = ?,update_time = ? where id = "+roleEntity.getId();

        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, roleEntity.getRoleName());
                        ps.setString(2, roleEntity.getDescription());
                        ps.setTimestamp(3, new Timestamp(roleEntity.getUpdateTime().getTime()));
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","修改表sys_role失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改sys_role表出错",e.toString()));
        }
        return result;
    }

    @Override
    public Integer deleteRole(Long id, List<Error> errorList) {
        int result = 0;
        try {
            String sql = "delete from sys_role where id =" +id;
            result = jdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_role数据出错",e.toString()));
        }
        return result;
    }
}
