package com.sugon.iris.sugondata.jdbcTemplate.impl.config;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondata.jdbcTemplate.intf.config.ConfigServiceDao;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.ConfigEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public class ConfigServiceDaoIntfImpl implements ConfigServiceDao {
    private static final Logger LOGGER = LogManager.getLogger(ConfigServiceDaoIntfImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 作者:SharpChan
     * 日期：2020.08.19
     * 描述：获取所有的配置信息
     */
    public List<ConfigEntity> getAllConfig(List<Error> errorList){
        String sql = "select id,cfg_key,cfg_value,flag,description,sort_no from config order by (sort_no+0) ASC";
        List<ConfigEntity> list = null;
        try{
            list = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(ConfigEntity.class));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"数据库查询配置信息config失败",e.toString()));
        }
        return list;
    }

     @Override
     public int  updateConfig(ConfigEntity configEntity,List<Error> errorList){
         int result=0;
         String sql = "UPDATE config SET cfg_key = ?,cfg_value = ?,flag = ?,updatetime = ?,description = ? ,userName = ? ,sort_no = ?  WHERE id = ?";
         try{
             result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                 @Override
                 public void setValues(PreparedStatement ps) throws SQLException {
                     if(!StringUtils.isEmpty(configEntity.getCfg_key()) ) {
                         ps.setString(1, configEntity.getCfg_key());
                     }else{
                         ps.setString(1, null);
                     }
                     if(!StringUtils.isEmpty(configEntity.getCfg_value())) {
                         ps.setString(2, configEntity.getCfg_value());
                     }else{
                         ps.setString(2, null);
                     }
                     if(null != configEntity.getFlag()) {
                         ps.setInt(3, configEntity.getFlag());
                     }else{
                         ps.setString(3, null);
                     }
                     ps.setTimestamp(4, new Timestamp(configEntity.getUpdateTime().getTime()));
                     if(!StringUtils.isEmpty(configEntity.getDescription())) {
                         ps.setString(5, configEntity.getDescription());
                     }else{
                         ps.setString(5, null);
                     }

                     if(!StringUtils.isEmpty(configEntity.getUserName())) {
                         ps.setString(6, configEntity.getUserName());
                     }else{
                         ps.setString(6, null);
                     }
                     if(!StringUtils.isEmpty(configEntity.getSortNo())) {
                         ps.setString(7, configEntity.getSortNo());
                     }else{
                         ps.setString(7, null);
                     }
                     ps.setLong(8, configEntity.getId());
                 }
             });
         }catch(Exception e){
             LOGGER.info("{}-{}","修改表config失败",e);
             errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改config表出错",e.toString()));
         }
         return result;
     }

    @Override
    public int saveConfig(ConfigEntity configEntity, List<Error> errorList) {
        int result=0;
        String sql = "insert into config(cfg_key,cfg_value,flag,description,createtime,userName,sort_no) values(?,?,?,?,?,?,?)";
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(configEntity.getCfg_key()) ) {
                        ps.setString(1, configEntity.getCfg_key());
                    }else{
                        ps.setString(1, null);
                    }
                    if(!StringUtils.isEmpty(configEntity.getCfg_value())) {
                        ps.setString(2, configEntity.getCfg_value());
                    }else{
                        ps.setString(2, null);
                    }
                    if(null != configEntity.getFlag()) {
                        ps.setInt(3, configEntity.getFlag());
                    }else{
                        ps.setString(3, null);
                    }
                    if(!StringUtils.isEmpty(configEntity.getDescription())) {
                        ps.setString(4, configEntity.getDescription());
                    }else{
                        ps.setString(4, null);
                    }
                    ps.setTimestamp(5, new Timestamp(configEntity.getCreateTime().getTime()));
                    if(!StringUtils.isEmpty(configEntity.getUserName())) {
                        ps.setString(6, configEntity.getUserName());
                    }else{
                        ps.setString(6, null);
                    }
                    if(!StringUtils.isEmpty(configEntity.getSortNo())) {
                        ps.setString(7, configEntity.getSortNo());
                    }else{
                        ps.setString(7, null);
                    }
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表config失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入config表出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteConfig(Long id, List<Error> errorList) {
        int result = 0;
        try {
            String sql = "delete from config where  id = " + id;
            result = jdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除config出错",e.toString()));
        }
        return result;
    }
}
