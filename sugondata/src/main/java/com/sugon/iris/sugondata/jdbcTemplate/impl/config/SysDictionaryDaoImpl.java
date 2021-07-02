package com.sugon.iris.sugondata.jdbcTemplate.impl.config;

import com.ctc.wstx.util.StringUtil;
import com.sugon.iris.sugondata.jdbcTemplate.intf.config.SysDictionaryDaoIntf;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.SysDictionaryEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class SysDictionaryDaoImpl implements SysDictionaryDaoIntf {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询所有的字典项
     */
    @Override
    public List<SysDictionaryEntity> findSysDictionary(String dicGroup,List<Error> errorList) {
        String sql = "select id,dic_group,value,dic_show,comment from sys_dictionary where 1=1 ";
        if(StringUtils.isNotEmpty(dicGroup)){
            sql += " and dic_group = '"+dicGroup+"' ";
        }
        List<SysDictionaryEntity> list = null;
        try{
            list = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SysDictionaryEntity.class));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"数据库查询配置信息sys_dictionary失败",e.toString()));
        }
        return list;
    }

    /**
     * 更新字典项
     *
     * @param sysDictionaryEntity
     */
    @Override
    public int updateSysDictionary(SysDictionaryEntity sysDictionaryEntity,List<Error> errorList) {
        int result=0;
        String sql = "UPDATE sys_dictionary SET dic_group = ?,value = ?,dic_show = ?,comment = ?,user_id = ?,update_time = ?  WHERE id = ?";
        Object[] objs = new Object[] {sysDictionaryEntity.getDicGroup(),sysDictionaryEntity.getValue(),
                                      sysDictionaryEntity.getDicShow(),sysDictionaryEntity.getComment(),
                                      sysDictionaryEntity.getUserId(),sysDictionaryEntity.getUpdateTime(),
                                      sysDictionaryEntity.getId()};
        try{
            result=jdbcTemplate.update(sql,objs);
        }catch(Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"更新配置信息表sys_dictionary出错",e.toString()));
        }
        return result;
    }

    /**
     * 保存字典项
     *
     * @param sysDictionaryEntity
     */
    @Override
    public int saveSysDictionary(SysDictionaryEntity sysDictionaryEntity,List<Error> errorList) {
        int result=0;
        String sql = "insert into sys_dictionary(dic_group,value,dic_show,comment,user_id,create_time) values(?,?,?,?,?,?)";
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(sysDictionaryEntity.getDicGroup()) ) {
                        ps.setString(1, sysDictionaryEntity.getDicGroup());
                    }else{
                        ps.setString(1, null);
                    }
                    if(!StringUtils.isEmpty(sysDictionaryEntity.getValue())) {
                        ps.setString(2, sysDictionaryEntity.getValue());
                    }else{
                        ps.setString(2, null);
                    }
                    if(!StringUtils.isEmpty(sysDictionaryEntity.getDicShow())) {
                        ps.setString(3, sysDictionaryEntity.getDicShow());
                    }else{
                        ps.setString(3, null);
                    }
                    if(!StringUtils.isEmpty(sysDictionaryEntity.getComment())) {
                        ps.setString(4, sysDictionaryEntity.getComment());
                    }else{
                        ps.setString(4, null);
                    }
                    if(null != sysDictionaryEntity.getUserId()) {
                        ps.setLong(5, sysDictionaryEntity.getUserId());
                    }else{
                        ps.setString(5, null);
                    }
                    ps.setTimestamp(6, new Timestamp(sysDictionaryEntity.getCreateTime().getTime()));
                }
            });
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入sys_dictionary表出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteSysDictionary(Long id, List<Error> errorList) {
        int result = 0;
        try {
            String sql = "delete from sys_dictionary where  id = " + id;
            result = jdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_dictionary出错",e.toString()));
        }
        return result;
    }
}
