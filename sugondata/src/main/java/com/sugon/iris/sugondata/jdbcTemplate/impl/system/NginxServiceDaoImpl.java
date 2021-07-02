package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.NginxServiceDaoIntf;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.securityModuleEntities.WhiteIpEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
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
import java.util.List;

@Repository
public class NginxServiceDaoImpl implements NginxServiceDaoIntf {
    private static final Logger LOGGER = LogManager.getLogger(NginxServiceDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<WhiteIpEntity> queryWhiteIp(WhiteIpEntity whiteIpEntity, List<Error> errorList) {
        List<WhiteIpEntity>  whiteIpEntityList = null;
        try{
            String sql = "select id, name,idcard,policeno,ip,mac,comment,createtime from white_ip where 1 = 1 ";
            if(null != whiteIpEntity) {
                if (!StringUtils.isEmpty(whiteIpEntity.getName())) {
                    sql = sql + " and name = " + whiteIpEntity.getName() + " ";
                }
                if (!StringUtils.isEmpty(whiteIpEntity.getIdCard())) {
                    sql = sql + " and idcard = " + whiteIpEntity.getIdCard() + " ";
                }
                if (!StringUtils.isEmpty(whiteIpEntity.getPoliceNo())) {
                    sql = sql + " and policeno = " + whiteIpEntity.getPoliceNo() + " ";
                }
                if (!StringUtils.isEmpty(whiteIpEntity.getIp())) {
                    sql = sql + " and ip = " + whiteIpEntity.getIp() + " ";
                }
                if (!StringUtils.isEmpty(whiteIpEntity.getMac())) {
                    sql = sql + " and mac = " + whiteIpEntity.getMac() + " ";
                }
            }
            whiteIpEntityList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(WhiteIpEntity.class));
        }catch(Exception e){
            LOGGER.info("{}-{}","查询表white_ip失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询white_ip表出错",e.toString()));
        }
        return  whiteIpEntityList;
    }

    @Override
    public int saveWhiteIp(WhiteIpEntity whiteIpEntity, List<Error> errorList) {
        int result=0;
        String sql = "insert into white_ip(name,idcard,policeno,ip,mac,comment,createtime) values(?,?,?,?,?,?,?)";
        try{
            result=jdbcTemplate.update(sql, new PreparedStatementSetter(){
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if(!StringUtils.isEmpty(whiteIpEntity.getName()) ) {
                        ps.setString(1, whiteIpEntity.getName());
                    }else{
                        ps.setString(1, "");
                    }
                    if(!StringUtils.isEmpty(whiteIpEntity.getIdCard())) {
                        ps.setString(2, whiteIpEntity.getIdCard());
                    }
                    if(!StringUtils.isEmpty(whiteIpEntity.getPoliceNo())) {
                        ps.setString(3, whiteIpEntity.getPoliceNo());
                    }else{
                        ps.setString(3, "");
                    }
                    if(!StringUtils.isEmpty(whiteIpEntity.getIp())) {
                        ps.setString(4, whiteIpEntity.getIp());
                    }
                    if(!StringUtils.isEmpty(whiteIpEntity.getMac())) {
                        ps.setString(5, whiteIpEntity.getMac());
                    }else{
                        ps.setString(5, "");
                    }
                    if(!StringUtils.isEmpty(whiteIpEntity.getComment())) {
                        ps.setString(6, whiteIpEntity.getComment());
                    }else{
                        ps.setString(6, "");
                    }
                    ps.setTimestamp(7, new Timestamp(whiteIpEntity.getCreateTime().getTime()));
                }
            });
        }catch(Exception e){
            LOGGER.info("{}-{}","插入表white_ip失败",e);
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入white_ip表出错",e.toString()));
        }
        return result;
    }

    @Override
    public int deleteWhiteIp(Long id, List<Error> errorList) {
        int result = 0;
        try {
            String sql = "delete from white_ip where  id = " + id;
            result = jdbcTemplate.update(sql);
        }catch(Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除白名单数据库出错",e.toString()));
        }
        return result;
    }
}
