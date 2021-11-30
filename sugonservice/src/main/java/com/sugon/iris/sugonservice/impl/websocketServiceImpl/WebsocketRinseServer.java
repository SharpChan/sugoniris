package com.sugon.iris.sugonservice.impl.websocketServiceImpl;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.sugon.iris.sugoncommon.baseConfig.SpringUtil;
import com.sugon.iris.sugondomain.beans.webSocket.WebSocketRequestDto;
import com.sugon.iris.sugondomain.enums.SZ_JZ_RinseType_Enum;
import com.sugon.iris.sugonservice.service.dataRinseService.DataRinseService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/websocketRinse/{userId}")
public class WebsocketRinseServer {

    private static final Logger LOGGER = LogManager.getLogger(WebsocketRinseServer.class);

    private static ConcurrentHashMap<String, WebsocketRinseServer> webSocketMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String userId = "";

    private static DataRinseService dataRinseServiceImpl;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId, Session session) {

        if(this.dataRinseServiceImpl == null){
            this.dataRinseServiceImpl = (DataRinseService) SpringUtil.getBean("dataRinseServiceImpl");
        }

        this.session = session;
        this.userId = userId;//接收到发送消息的人员编号
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
        }
        webSocketMap.put(userId, this);     //加入set中
        System.out.println("连接成功");
        try {
            sendMessage("连接成功");
        }catch (IOException e) {
            LOGGER.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);  //从set中删除
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                Gson gson = new Gson();
                WebSocketRequestDto<Long> webSocketRequestDto = gson.fromJson(message, new TypeToken<WebSocketRequestDto<Long>>(){}.getType());
                if(SZ_JZ_RinseType_Enum.RINSE_01.getCode().equals(webSocketRequestDto.getBusinessId())){
                    //dataRinseServiceImpl.completeRinse(userId,webSocketRequestDto.getParam());
                }
                //传送给对应toUserId用户的websocket
                if(!webSocketMap.containsKey(String.valueOf(webSocketRequestDto.getUserId()))){
                    webSocketMap.get(String.valueOf(webSocketRequestDto.getUserId())).sendMessage("请求的userId:"+webSocketRequestDto.getUserId()+"不在该服务器上");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        LOGGER.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
           // LOGGER.error("用户"+userId+",不在线！");
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送信息给所有人
     * @param message
     * @throws IOException
     */
    public void sendtoAll(String message) throws IOException {
        for (String key : webSocketMap.keySet()) {
            try {
                webSocketMap.get(key).sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
