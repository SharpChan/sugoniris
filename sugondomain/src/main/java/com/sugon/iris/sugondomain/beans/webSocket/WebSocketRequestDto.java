package com.sugon.iris.sugondomain.beans.webSocket;

import lombok.Data;

@Data
public class WebSocketRequestDto<T> {

    /**
     *用户编号
     */
   private Long userId;

   private String  businessId;

   private T  param;
}
