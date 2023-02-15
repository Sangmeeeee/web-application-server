package util;

import session.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class HttpSessionUtils {
    private static Map<String, HttpSession> sessions = new HashMap<>();

    public static void addSession(HttpSession session){
        sessions.put(session.getId(), session);
    }

    public static HttpSession getSession(String sessionId){
        HttpSession session = sessions.get(sessionId);
        if(session == null){
            session = new HttpSession(sessionId);
            sessions.put(sessionId, session);
        }
        return session;
    }

    public static void remove(String sessionId){
        sessions.remove(sessionId);
    }
}
