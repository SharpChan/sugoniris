package com.sugon.iris.sugonlistener.common;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.configDtos.ConfigDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.configEntities.ConfigEntity;
import com.sugon.iris.sugonservice.service.configService.ConfigService;
import com.sugon.iris.sugonservice.service.kafkaService.KafkaStartStopService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@WebListener
@Configuration
@EnableScheduling   // 2.开启定时任务
public class CommonConfigurationListener implements ServletContextListener {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KafkaStartStopService kafkaStartStopService;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
        try {
            getConfigBean();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

    @Scheduled(fixedRate=1000)
  public void getConfigBean() throws Exception{
        java.sql.Connection connection = null;
        try {
            //进行系统配置项配置
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
            //开启或者关闭rocket
            if("0".equals(PublicUtils.getConfigMap().get("rocketMq.consumer.onOff"))){//进行关闭
               if(PublicUtils.rocketMqConsumer != null ){//当前是打开状态
                   PublicUtils.rocketMqConsumer.shutdown();
                   PublicUtils.rocketMqConsumer = null;
               }
            }
            if("1".equals(PublicUtils.getConfigMap().get("rocketMq.consumer.onOff"))){//进行开启
                if(PublicUtils.rocketMqConsumer == null ){//当前是关闭状态
                    String rocketMq_NameSvrAddr = PublicUtils.getConfigMap().get("rocketMq.NameSvrAddr");

                    String consumerGroup = PublicUtils.getConfigMap().get("rocketMq.consumerGroup");

                    String topic = PublicUtils.getConfigMap().get("rocketMq.topic");

                    String tag = PublicUtils.getConfigMap().get("rocketMq.tag");

                    // 设置消费者组
                    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

                    consumer.setVipChannelEnabled(false);
                    consumer.setNamesrvAddr(rocketMq_NameSvrAddr);
                    // 设置消费者端消息拉取策略，表示从哪里开始消费
                    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

                    // 设置消费者拉取消息的策略，*表示消费该topic下的所有消息，也可以指定tag进行消息过滤
                    consumer.subscribe(topic, "*");

                    // 消费者端启动消息监听，一旦生产者发送消息被监听到，就打印消息，和rabbitmq中的handlerDelivery类似
                    consumer.registerMessageListener(new MessageListenerConcurrently() {

                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                            for (MessageExt messageExt : msgs) {
                                String topic = messageExt.getTopic();
                                String tag = messageExt.getTags();
                                String msg = new String(messageExt.getBody());
                                System.out.println("*********************************");
                                System.out.println("消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + msg + ", tag:" + tag + ", topic:" + topic);
                                System.out.println("*********************************");
                            }
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    });
                    PublicUtils.rocketMqConsumer = consumer;
                    consumer.start();
                }
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
