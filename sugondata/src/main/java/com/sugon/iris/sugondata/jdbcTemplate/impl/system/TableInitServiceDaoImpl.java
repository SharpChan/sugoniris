package com.sugon.iris.sugondata.jdbcTemplate.impl.system;

import com.sugon.iris.sugondata.jdbcTemplate.intf.system.AccountServiceDaoIntf;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.TableInitServiceDaoIntf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Service
public class TableInitServiceDaoImpl implements TableInitServiceDaoIntf {

    private static final Logger LOGGER = LogManager.getLogger(TableInitServiceDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountServiceDaoIntf accountServiceDaoImpl;

    @Override
    public void createTables() {
        java.sql.Connection connection = null;
        try {
            DataSource dataSource = accountServiceDaoImpl.getJdbcTemplate().getDataSource();
            connection = dataSource.getConnection();
            DatabaseMetaData meta = connection.getMetaData();
            java.sql.ResultSet table_user = meta.getTables(null, null, "sys_user", null);
            if (!table_user.next()) {
                String SQL="CREATE TABLE `sys_user` (\n" +
                        "  `id` bigint(20) NOT NULL COMMENT '用户id',\n" +
                        "  `email` varchar(200) NOT NULL COMMENT '邮箱',\n" +
                        "  `password` varchar(100) NOT NULL COMMENT '密码',\n" +
                        "  `imageurl` varchar(200) DEFAULT NULL COMMENT '头像地址',\n" +
                        "  `createtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',\n" +
                        "  `updatetime` timestamp NULL DEFAULT NULL COMMENT '修改时间',\n" +
                        "  `flag` int(11) NOT NULL COMMENT '0：无效 1：有效 2:待审核'\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
                jdbcTemplate.execute(SQL);
            }
            java.sql.ResultSet table_sequence = meta.getTables(null, null, "sys_sequence", null);
            if (!table_sequence.next()) {
                String SQL_sequence="CREATE TABLE `sys_sequence` (\n" +
                        "  `name` varchar(50) NOT NULL,\n" +
                        "  `current_value` BIGINT(20) NOT NULL,\n" +
                        "  `increment` BIGINT(20) NOT NULL DEFAULT '1',\n" +
                        "  PRIMARY KEY (`name`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
                jdbcTemplate.execute(SQL_sequence);

                String SQL_currval="CREATE DEFINER=`root`@`%` FUNCTION `iris`.`currval`(seq_name VARCHAR(50)) RETURNS bigint(20)\n" +
                        "BEGIN\n" +
                        "     DECLARE value BIGINT;\n" +
                        "     SELECT current_value INTO value\n" +
                        "     FROM sys_sequence\n" +
                        "     WHERE upper(name) = upper(seq_name); -- 大小写不区分.\n" +
                        "     RETURN value;\n" +
                        "END";
                jdbcTemplate.execute(SQL_currval);

                String SQL_nextval="CREATE DEFINER=`root`@`%` FUNCTION `iris`.`nextval`(seq_name VARCHAR(50)) RETURNS bigint(20)\n" +
                        "BEGIN\n" +
                        "     DECLARE value BIGINT;\n" +
                        "     UPDATE sys_sequence\n" +
                        "     SET current_value = current_value + increment \n" +
                        "     WHERE upper(name) = upper(seq_name);\n" +
                        "     RETURN currval(seq_name); \n" +
                        "END";
                jdbcTemplate.execute(SQL_nextval);

                String SQL_setval="CREATE DEFINER=`root`@`%` FUNCTION `iris`.`setval`(seq_name VARCHAR(50), value BIGINT) RETURNS bigint(20)\n" +
                        "BEGIN\n" +
                        "     UPDATE sys_sequence\n" +
                        "     SET current_value = value \n" +
                        "     WHERE upper(name) = upper(seq_name); \n" +
                        "     RETURN currval(seq_name); \n" +
                        "END";
                jdbcTemplate.execute(SQL_setval);
                accountServiceDaoImpl.initSeq();
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.toString());
        }finally {
            if(connection !=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
