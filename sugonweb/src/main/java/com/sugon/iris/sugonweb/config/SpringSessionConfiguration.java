package com.sugon.iris.sugonweb.config;

import com.sugon.iris.sugoncommon.session.MySessionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.events.AbstractSessionEvent;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;

/**
 * session整合配置类
 *
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 36000)
public class SpringSessionConfiguration {
    private MySessionContext myc = MySessionContext.getInstance();
     /**
      * Redis内session过期事件监听
      */
      @EventListener
      public void onSessionExpired(SessionExpiredEvent expiredEvent) {
          Session session = expiredEvent.getSession();
          myc.delSession(session);
      }


       /**
      * Redis内session删除事件监听
      */
       @EventListener
      public void onSessionDeleted(SessionDeletedEvent deletedEvent) {
           Session session = deletedEvent.getSession();
           myc.delSession(session);

      }

       /**
      * Redis内session保存事件监听
      */
      @EventListener
      public void onSessionCreated(SessionCreatedEvent createdEvent) {
           Session session = createdEvent.getSession();
           myc.addSession(session);
      }

      @EventListener
    public void getSession(AbstractSessionEvent abstractSessionEvent){
          Session session = abstractSessionEvent.getSession();
      }

}
