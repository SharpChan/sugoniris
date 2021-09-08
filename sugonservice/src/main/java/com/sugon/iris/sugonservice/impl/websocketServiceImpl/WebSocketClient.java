package com.sugon.iris.sugonservice.impl.websocketServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;

@ClientEndpoint
public class WebSocketClient  {
    private static Logger LOG = LogManager.getLogger(WebSocketServer.class);
    private Session session;
    @OnOpen
    public void open(Session session){
        LOG.info("Client WebSocket is opening...");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message){
        LOG.info("Server send message: " + message);
    }

    @OnClose
    public void onClose(){
        LOG.info("Websocket closed");
    }

    /**
     * 发送客户端消息到服务端
     * @param message 消息内容
     */
    public void send(String message){
        this.session.getAsyncRemote().sendText(message);
    }
}
