package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.MenuServiceDaoIntf;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository
public class MenuServiceDaoImpl implements MenuServiceDaoIntf {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public  List<MenuEntity> getMenuInfos(MenuEntity menuEntity,List<Error> errorList){
        List<MenuEntity>  menuEntityList = null;
        String sql = "SELECT id,name,father_id ,`text` ,`translate` ,heading ,sref,icon,alert,label,user_id,createtime,updatetime,tier,sort FROM sys_menu sm where 1=1";
        if(menuEntity != null ){
            if(null != menuEntity.getId()){
                sql += " and id = "+menuEntity.getId();
            }
            if(null !=menuEntity.getFatherId()){
                sql += " and father_id = "+menuEntity.getFatherId();
            }
            if(null != menuEntity.getTier()){
                sql += " and tier = "+menuEntity.getTier();
            }
        }
        try{
            menuEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(MenuEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表sys_menu失败",e);
            errorList.add(new Error("{SYS-02-006}","查询sys_menu表出错",e.toString()));
        }

        return  menuEntityList;
    }

    @Override
    public int deleteMenu(Long id,List<Error> errorList){
        int result=0;
        if(null == id){
           return result;
        }
        String sql = "delete from sys_menu where  id = " +id;
        try {
            result = jdbcTemplate.update(sql);
        }catch (Exception e){
            errorList.add(new Error("{SYS-02-007}","删除表sys_menu出错",e.toString()));
        }
        return result;
    }

    @Override
    public int insertMenu(MenuEntity menuEntity,List<Error> errorList){
        int result=0;
        if(null == menuEntity){
            return result;
        }
        String sql = "insert into sys_menu(name,father_id ,text ,translate,heading ,sref,icon,alert,label,user_id,createtime,tier,sort) " +
                     "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(menuEntity.getName())) {
                        ps.setString(1, menuEntity.getName());
                    }else{
                        ps.setNull(1,Types.VARCHAR);
                    }
                    if(null != menuEntity.getFatherId()) {
                        ps.setLong(2, menuEntity.getFatherId());
                    }else{
                        ps.setNull(2,Types.BIGINT);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getText())) {
                        ps.setString(3, menuEntity.getText());
                    }else{
                        ps.setNull(3,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getTranslate())) {
                        ps.setString(4, menuEntity.getTranslate());
                    }else{
                        ps.setNull(4,Types.VARCHAR);
                    }
                    if(null != menuEntity.getHeading()){
                        ps.setBoolean(5, menuEntity.getHeading());
                    }else{
                        ps.setNull(5,Types.BOOLEAN);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getSref())) {
                        ps.setString(6, menuEntity.getSref());
                    }else{
                        ps.setNull(6,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getIcon())) {
                        ps.setString(7, menuEntity.getIcon());
                    }else{
                        ps.setNull(7,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getAlert())) {
                        ps.setString(8, menuEntity.getAlert());
                    }else{
                        ps.setNull(8,Types.VARCHAR);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getLabel())) {
                        ps.setString(9, menuEntity.getLabel());
                    }else{
                        ps.setNull(9,Types.VARCHAR);
                    }
                    if(null != menuEntity.getUserId()) {
                        ps.setLong(10, menuEntity.getUserId());
                    }else{
                        ps.setNull(10,Types.BIGINT);
                    }
                        ps.setTimestamp(11, new Timestamp(menuEntity.getCreateTime().getTime()));
                    if(null != menuEntity.getTier()) {
                        ps.setInt(12, menuEntity.getTier());
                    }else{
                        ps.setNull(12,Types.INTEGER);
                    }
                    if(null != menuEntity.getSort()) {
                        ps.setInt(13, menuEntity.getSort());
                    }else{
                        ps.setNull(13,Types.INTEGER);
                    }
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表menu失败",e);
            errorList.add(new Error("{SYS-02-008}","插入user表出错",e.toString()));
        }
        return result;
    }

    @Override
    public int updateMenu(MenuEntity menuEntity,List<Error> errorList){
        int result=0;
        String sql = "update sys_menu set name=?,father_id = ? ,`text` = ? ,`translate` = ? ,heading = ?,sref = ?,icon = ?,alert = ?,label = ?,user_id = ?,updatetime = ? where id = "+menuEntity.getId();
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(null != menuEntity.getName()) {
                        ps.setString(1, menuEntity.getName());
                    }
                    if(null != menuEntity.getFatherId()) {
                        ps.setLong(2, menuEntity.getFatherId());
                    }
                    if(!StringUtils.isEmpty(menuEntity.getText())) {
                        ps.setString(3, menuEntity.getText());
                    }
                    if(!StringUtils.isEmpty(menuEntity.getTranslate())) {
                        ps.setString(4, menuEntity.getTranslate());
                    }
                    ps.setBoolean(5, menuEntity.getHeading());

                    if(!StringUtils.isEmpty(menuEntity.getSref())) {
                        ps.setString(6, menuEntity.getSref());
                    }else{
                        ps.setString(6, null);
                    }
                    if(!StringUtils.isEmpty(menuEntity.getIcon())) {
                        ps.setString(7, menuEntity.getIcon());
                    }
                    if(!StringUtils.isEmpty(menuEntity.getAlert())) {
                        ps.setString(8, menuEntity.getAlert());
                    }

                    ps.setLong(9, menuEntity.getUserId());

                    ps.setTimestamp(10, new Timestamp(new Date().getTime()));
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表sys_menu失败",e);
            errorList.add(new Error("{SYS-02-002}","修改user表出错",e.toString()));
        }
        return result;
    }

    /**
     * 获取menu表序列
     * @param errorList
     * @return
     */
    @Override
    public Long getMenu_seq(List<Error> errorList){
        Long result = null;
        try {
            String sql = "select nextval('menu_seq')";
            result = jdbcTemplate.queryForObject(sql,Long.class);
        }catch (Exception e){
            LOGGER.error(e.toString());
            errorList.add(new Error("{SYS-02-009}","menu_seq",e.toString()));
        }
        return result;
    }
    /**
     * 初始化menu表序列
     */
    @Transactional
    @Override
    public void initMenuSeq() {
        String SQL_init_menu_seq="insert into sequence set name='menu_seq',current_value=1000000000";
        jdbcTemplate.update(SQL_init_menu_seq);
    }

}
