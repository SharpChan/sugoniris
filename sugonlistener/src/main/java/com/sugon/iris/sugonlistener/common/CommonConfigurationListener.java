package com.sugon.iris.sugonlistener.common;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.ConfigEntity;
import com.sugon.iris.sugonservice.service.kafkaService.KafkaStartStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.List;

/**
 *
 */
@Configuration
@EnableScheduling   // 2.开启定时任务
public class CommonConfigurationListener {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KafkaStartStopService kafkaStartStopService;

    @Scheduled(fixedRate=1000)
  public void getConfigBean() throws Exception{
        java.sql.Connection connection = null;
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            connection = dataSource.getConnection();
            DatabaseMetaData meta = connection.getMetaData();
            java.sql.ResultSet tables = meta.getTables (null, null, "config", null);

            if (tables.next()) {

               String sql = "select cfg_key ,cfg_value from config where flag = 1 ";
                List<ConfigEntity> list = null;
                try {
                    list = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(ConfigEntity.class));
                    if(!CollectionUtils.isEmpty(list)){
                        for(ConfigEntity obj :list){
                            PublicUtils.getConfigMap().put(obj.getCfg_key(),obj.getCfg_value());
                            kafkaStopStart(obj);//对kafka的启停进行控制
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                String SQL="CREATE TABLE `config` (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "  `cfg_key` varchar(200) DEFAULT NULL COMMENT 'key',\n" +
                    "  `cfg_value` varchar(200) DEFAULT NULL COMMENT 'value',\n" +
                    "  `createtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `flag` int(11) DEFAULT NULL COMMENT '可用标志1：可用，0：不可用',\n" +
                    "  `updatetime` timestamp NULL DEFAULT NULL COMMENT '修改时间',\n" +
                    "  `userName` varchar(100) DEFAULT NULL COMMENT '操作人',\n" +
                    "  `description` varchar(200) DEFAULT NULL COMMENT '描述',\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8";
                jdbcTemplate.execute(SQL);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }finally {
            if(connection !=null) {
                connection.close();
            }
        }
  }

    /**
     * kafka启动和停止
     * 不配置默认开启
     */
  private void kafkaStopStart(ConfigEntity obj){
      if (obj.getCfg_key().contains("_kafka")){
          if("0".equals(obj.getCfg_value())){
              kafkaStartStopService.stopListener(obj.getCfg_value());
          }else if("1".equals(obj.getCfg_value())){
              kafkaStartStopService.startListener(obj.getCfg_value(),"1");
          }
      }
  }
}
