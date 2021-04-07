package com.sugon.iris.sugoncommon.session;

import org.springframework.session.Session;

import java.util.concurrent.ConcurrentHashMap;

public class MySessionContext {
    private static MySessionContext instance;
    private ConcurrentHashMap<String, Session> sessionMap;

    private MySessionContext() {

        sessionMap = new ConcurrentHashMap<String,Session>();
    }

    public static MySessionContext getInstance() {
        if (instance == null) {
            instance = new MySessionContext();
        }
        return instance;
    }

    public synchronized void addSession(Session session) {
        if (session != null) {
            sessionMap.put(session.getId(), session);
        }
    }

    public synchronized void delSession(Session session) {
        if (session != null) {
            sessionMap.remove(session.getId());
        }
    }

    public synchronized Session getSession(String sessionID) {
        if (sessionID == null) {
            return null;
        }
        return sessionMap.get(sessionID);
    }

}
