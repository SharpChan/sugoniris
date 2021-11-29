package com.sugon.iris.sugonfilerest.FileData2Mpp;

import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, InterruptedException {
        // 需要一个producer group名字作为构造方法的参数，这里为producer1
        DefaultMQProducer producer = new DefaultMQProducer("producer1");

        // 设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        // NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr("192.168.217.135:9876");
        producer.setVipChannelEnabled(false);

        // 为避免程序启动的时候报错，添加此代码，可以让rocketMq自动创建topickey
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        producer.start();

        for (int i = 0; i < 1; i++) {
            try {
                // topic 主题名称
                // pull 临时值 在消费者消费的时候 可以根据msg类型进行消费
                // body 内容

                Map<String,Object> map = new HashMap<>();
                String token = "32045406202111081541272021-11-30 23:11:30";
                map.put("token",token);
                map.put("policeno","1234567");

                List<Map<String,String>> list_01 = new ArrayList<>();
                Map<String,String> map3_1_1 = new HashMap<>();
                map3_1_1.put("khmc","张三");
                map3_1_1.put("zzlx","身份证");
                map3_1_1.put("lxdh","18761707610");
                map3_1_1.put("zzh","320683198810192038");
                list_01.add(map3_1_1);
                Map<String,String> map3_1_2 = new HashMap<>();
                map3_1_2.put("khmc","李四");
                map3_1_2.put("zzlx","身份证");
                map3_1_2.put("lxdh","18761707611");
                map3_1_2.put("zzh","320683198810192039");
                list_01.add(map3_1_2);
                Map<String,String> map3_1_3 = new HashMap<>();
                map3_1_3.put("khmc","王五");
                map3_1_3.put("zzlx","身份证");
                map3_1_3.put("lxdh","18761707612");
                map3_1_3.put("zzh","320683198810192039");
                list_01.add(map3_1_3);


                List<Map<String,String>> list_02 = new ArrayList<>();
                Map<String,String> map3_2_1 = new HashMap<>();
                map3_2_1.put("khmc","张三");
                map3_2_1.put("zzlx","身份证");
                map3_2_1.put("zzdz","aaaa");
                map3_2_1.put("zzhm","320683198810192038");
                list_02.add(map3_2_1);
                Map<String,String> map3_2_2 = new HashMap<>();
                map3_2_2.put("khmc","李四");
                map3_2_2.put("zzlx","身份证");
                map3_2_2.put("zzdz","bbbb");
                map3_2_2.put("zzhm","320683198810192039");
                list_02.add(map3_2_2);
                Map<String,String> map3_2_3 = new HashMap<>();
                map3_2_3.put("khmc","王五");
                map3_2_3.put("zzlx","身份证");
                map3_2_3.put("zzdz","cccc");
                map3_2_3.put("zzhm","320683198810192039");
                list_02.add(map3_2_3);

                Map<String ,List<Map<String,String>>> map2_1 = new HashMap();
                map2_1.put("19",list_01);

                Map<String ,List<Map<String,String>>> map2_2 = new HashMap();
                map2_2.put("20",list_02);

                List<Map>  listAll = new ArrayList<>();
                listAll.add(map2_1);
                listAll.add(map2_2);

                map.put("3204540620211108154127",listAll);
                Gson gson = new Gson();
                String mapJson =  gson.toJson(map);

                Message message = new Message("broker-a", "msg", mapJson.getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(message);

                System.out.println("发送的消息ID:" + sendResult.getMsgId() + "--- 发送消息的状态：" + sendResult.getSendStatus());
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        producer.shutdown();
    }
}
