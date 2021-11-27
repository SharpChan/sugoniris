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

        for (int i = 0; i < 100; i++) {
            try {
                // topic 主题名称
                // pull 临时值 在消费者消费的时候 可以根据msg类型进行消费
                // body 内容

                Map<String,Object> map = new HashMap<>();
                String token = "32045406202111081541272021-11-26 18:11:30";
                map.put("token",token);
                map.put("policeno","1234567");

                List<Map<String,String>> list = new ArrayList<>();
                Map<String,String> map3_1 = new HashMap<>();
                map3_1.put("a1","b1");
                map3_1.put("a2","b2");
                map3_1.put("a3","b3");
                list.add(map3_1);
                Map<String,String> map3_2 = new HashMap<>();
                map3_2.put("a1","b1");
                map3_2.put("a2","b2");
                map3_2.put("a3","b3");
                list.add(map3_2);
                Map<String,String> map3_3 = new HashMap<>();
                map3_3.put("a1","b1");
                map3_3.put("a2","b2");
                map3_3.put("a3","b3");
                list.add(map3_3);

                Map<String ,List<Map<String,String>>> map2 = new HashMap();
                map2.put("19",list);
                map2.put("20",list);
                map2.put("21",list);

                map.put("3204540620211108154127",map2);
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
