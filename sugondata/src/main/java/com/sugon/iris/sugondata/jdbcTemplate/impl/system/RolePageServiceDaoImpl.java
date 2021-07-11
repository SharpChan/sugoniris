package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.RolePageServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.RolePageEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class RolePageServiceDaoImpl implements RolePageServiceDao {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int[] saveRolePages(List<RolePageEntity> rolePageEntityList, List<Error> errorList) {

        int[] result=null;

        if(CollectionUtils.isEmpty(rolePageEntityList)){
            return result;
        }
        String sql = "insert into sys_role_page(role_id,menu_id,create_time) " +
                "values(?,?,?)";

        try{
            result=jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){



                @Override
                public int getBatchSize() {
                    return rolePageEntityList.size();
                }

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, rolePageEntityList.get(i).getRoleId());
                    ps.setLong(2, rolePageEntityList.get(i).getMenuId());
                    ps.setTimestamp(3, new Timestamp( rolePageEntityList.get(i).getCreateTime().getTime()));
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表sys_role_page失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入sys_role_page表出错",e.toString()));
        }
        return result;

    }

    @Override
    public int[] deleteRolePages(List<Object[]> idList, List<Error> errorList) {
        int[] ints = null;
        //1.创建sql语句
        String sql = "delete from sys_role_page where role_id =?  and menu_id =?";
        //2.调用方法实现
        try {
            ints = jdbcTemplate.batchUpdate(sql, idList);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_role_page表出错",e.toString()));
        }
        return ints;
    }

    @Override
    public List<RolePageEntity> getRolePageByRoleId(Long roleId, List<Error> errorList) {

        List<RolePageEntity>  rolePageEntityList = null;
        String sql = "SELECT * FROM sys_role_page  where 1=1 ";

        if(null != roleId){
            sql += " and role_id = "+roleId;
        }
        try{
            rolePageEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(RolePageEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_role_page失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询sys_role_page表出错",e.toString()));
        }

        return  rolePageEntityList;
    }
}
