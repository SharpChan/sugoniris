package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.TranslateServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.TranslateEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.apache.commons.lang.StringUtils;
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

import java.sql.*;
import java.util.List;

@Service
public class TranslateServiceDaoImpl implements TranslateServiceDao {

    private static final Logger LOGGER = LogManager.getLogger(MenuServiceDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TranslateEntity> getZhCnTranslate(TranslateEntity translateEntity4Sql, List<Error> errorList) {
        List<TranslateEntity>  translateEntityList = null;
        String sql = "SELECT * FROM sys_translate  where 1=1";
        if(null != translateEntity4Sql && StringUtils.isNotEmpty(translateEntity4Sql.getTsType())){
                sql += " and ts_type = '"+translateEntity4Sql.getTsType()+"'";
        }
        if(null != translateEntity4Sql &&  null != translateEntity4Sql.getFatherId()){
                sql += " and father_id = "+translateEntity4Sql.getFatherId();
        }
        if(null != translateEntity4Sql && StringUtils.isNotEmpty(translateEntity4Sql.getSource())){
                sql += " and source = '"+translateEntity4Sql.getSource()+"'";
        }
        if(null != translateEntity4Sql && StringUtils.isNotEmpty(translateEntity4Sql.getGrade())){
            sql += " and grade = '"+translateEntity4Sql.getGrade()+"'";
        }
        if(null != translateEntity4Sql &&  null != translateEntity4Sql.getMenuId()){
            sql += " and menu_id = "+translateEntity4Sql.getMenuId();
        }
        try{
            translateEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(TranslateEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_translate失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询sys_translate表出错",e.toString()));
        }

        return  translateEntityList;
    }

    @Override
    public int saveTranslate(TranslateEntity translateEntity4Sql, List<Error> errorList) {
        int result=0;
        String sql =  "insert into sys_translate(father_id,source,value,grade,ts_type,menu_id) " +
                "values(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            result=jdbcTemplate.update(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps  = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                            ps.setLong(1, translateEntity4Sql.getFatherId());
                            ps.setString(2, translateEntity4Sql.getSource());
                            ps.setString(3, translateEntity4Sql.getValue());
                            ps.setString(4, translateEntity4Sql.getGrade());
                            ps.setString(5, translateEntity4Sql.getTsType());
                            ps.setLong(6, translateEntity4Sql.getMenuId());
                            return ps;
                        }
                    }, keyHolder);
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表sys_translate失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入sys_translate表出错",e.toString()));
        }
        translateEntity4Sql.setId(keyHolder.getKey().longValue());
        return result;

    }

    @Override
    public Integer updateTranslate(TranslateEntity translateEntity4Sql, List<Error> errorList) {
        int result=0;
        if(null == translateEntity4Sql){
            return result;
        }
        String sql = "update sys_translate set father_id = ?,source = ?,value = ? where id = "+translateEntity4Sql.getId();

        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setLong(1, translateEntity4Sql.getFatherId());
                    ps.setString(2, translateEntity4Sql.getSource());
                    ps.setString(3, translateEntity4Sql.getValue());
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","修改表sys_translate失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改sys_translate表出错",e.toString()));
        }
        return result;
    }

    @Override
    public int[] deleteTranslate(List<Object[]> idList, List<Error> errorList) {
        int[] ints = null;
        //1.创建sql语句
        String sql = "delete from sys_translate where id =?";
        //2.调用方法实现
        try {
            ints = jdbcTemplate.batchUpdate(sql, idList);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除sys_translate表出错",e.toString()));
        }
        return ints;
    }
}
