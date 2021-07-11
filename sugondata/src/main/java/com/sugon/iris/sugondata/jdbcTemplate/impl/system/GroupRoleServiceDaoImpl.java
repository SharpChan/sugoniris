package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.GroupRoleServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.GroupRoleEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
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
import java.util.List;

@Repository
public class GroupRoleServiceDaoImpl implements GroupRoleServiceDao {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer saveGroupRole(GroupRoleEntity groupRoleEntity, List<Error> errorList) {

        int result=0;
        String sql = "insert into sys_group_role(group_id,role_id,create_time) " +
                "values(?,?,?)";
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setLong(1, groupRoleEntity.getGroupId());
                    ps.setLong(2, groupRoleEntity.getRoleId());
                    ps.setTimestamp(3, new Timestamp( groupRoleEntity.getCreateTime().getTime()));
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表sys_group_role失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入sys_group_role表出错",e.toString()));
        }
        return result;

    }

    @Override
    public Integer deleteGroupRole(GroupRoleEntity groupRoleEntity, List<Error> errorList) {
        int i = 0;
        //1.创建sql语句
        String sql = "delete from sys_group_role where group_id="+groupRoleEntity.getGroupId() +" and role_id = "+groupRoleEntity.getRoleId();
        //2.调用方法实现
        try {
            i = jdbcTemplate.update(sql);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_role_page表出错",e.toString()));
        }
        return i;
    }

    @Override
    public List<GroupRoleEntity> getGroupRole(Long groupId, List<Error> errorList) {


        List<GroupRoleEntity>  groupRoleEntityList = null;
        String sql = "SELECT * FROM sys_group_role  where 1=1 ";

        if(null != groupId){
            sql += " and group_id = "+groupId;
        }
        try{
            groupRoleEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(GroupRoleEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_group_role失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询sys_group_role表出错",e.toString()));
        }

        return  groupRoleEntityList;
    }


}
