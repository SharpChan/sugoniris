package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.MenuServiceDaoIntf;
import com.sugon.iris.sugondomain.beans.menuBeans.Menu;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.menuEntities.MenuEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MenuServiceDaoImpl implements MenuServiceDaoIntf {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public  List<MenuEntity> getMenuInfos(Menu menu,List<Error> errorList){
        List<MenuEntity>  menuEntityList = null;
        String sql = "SELECT id,father_id ,`text` ,`translate` ,heading ,sref,icon,alert,label,menutype,create_user_id,createtime,updatetime FROM sys_menu sm where 1=1";
        if(menu != null ){
            if(null != menu.getId()){
                sql += " and id = "+menu.getId();
            }
            if(null !=menu.getFatherId()){
                sql += " and father_id = "+menu.getFatherId();
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
    public int insertMenu(MenuEntity MenuEntity,List<Error> errorList){
        int result=0;
        if(null == MenuEntity){
            return result;
        }
        String sql = "insert into sys_menu(id,father_id ,`text` ,`translate` ,heading ,sref,icon,alert,label,menutype,create_user_id,createtime,updatetime) " +
                     "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(null != MenuEntity.getId()) {
                        ps.setLong(1, MenuEntity.getId());
                    }
                    if(null != MenuEntity.getFatherId()) {
                        ps.setLong(2, MenuEntity.getFatherId());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getText())) {
                        ps.setString(3, MenuEntity.getText());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getTranslate())) {
                        ps.setString(4, MenuEntity.getTranslate());
                    }

                    ps.setBoolean(5, MenuEntity.getHeading());

                    if(!StringUtils.isEmpty(MenuEntity.getSref())) {
                        ps.setString(6, MenuEntity.getSref());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getIcon())) {
                        ps.setString(7, MenuEntity.getIcon());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getAlert())) {
                        ps.setString(8, MenuEntity.getAlert());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getLabel())) {
                        ps.setString(9, MenuEntity.getLabel());
                    }
                    if(null !=MenuEntity.getMenuType()) {
                        ps.setString(10, MenuEntity.getMenuType().getValue());
                    }

                    ps.setLong(11, MenuEntity.getCreateUserId());

                    ps.setTimestamp(12, new Timestamp(MenuEntity.getCreateTime().getTime()));
                }
            });
        }catch (Exception e){
            LOGGER.info("{}-{}","插入表menu失败",e);
            errorList.add(new Error("{SYS-02-008}","插入user表出错",e.toString()));
        }
        return result;
    }

    @Override
    public int updateMenu(MenuEntity MenuEntity,List<Error> errorList){
        int result=0;
        String sql = "update sys_menu set father_id = ? ,`text` = ? ,`translate` = ? ,heading = ?,sref = ?,icon = ?,alert = ?,label = ?,menutype = ?,create_user_id = ?,updatetime = ? where id = "+MenuEntity.getId();
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(null != MenuEntity.getFatherId()) {
                        ps.setLong(1, MenuEntity.getFatherId());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getText())) {
                        ps.setString(2, MenuEntity.getText());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getTranslate())) {
                        ps.setString(3, MenuEntity.getTranslate());
                    }
                    ps.setBoolean(4, MenuEntity.getHeading());

                    if(!StringUtils.isEmpty(MenuEntity.getSref())) {
                        ps.setString(5, MenuEntity.getSref());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getIcon())) {
                        ps.setString(6, MenuEntity.getIcon());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getAlert())) {
                        ps.setString(7, MenuEntity.getAlert());
                    }
                    if(!StringUtils.isEmpty(MenuEntity.getLabel())) {
                        ps.setString(8, MenuEntity.getLabel());
                    }
                    if(null !=MenuEntity.getMenuType()) {
                        ps.setString(9, MenuEntity.getMenuType().getValue());
                    }

                    ps.setLong(10, MenuEntity.getCreateUserId());

                    ps.setTimestamp(11, new Timestamp(new Date().getTime()));
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表user失败",e);
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
