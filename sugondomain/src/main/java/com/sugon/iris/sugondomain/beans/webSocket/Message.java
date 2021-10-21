package com.sugon.iris.sugondomain.beans.webSocket;

import lombok.Data;

import java.util.Map;

@Data
public class Message {

    private String event;

    private Map<String,String> data;
}
