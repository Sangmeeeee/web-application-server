package session;

import util.HttpSessionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {
    private String id;
    private Map<String, Object> attributes = new HashMap<>();

    public HttpSession(String sessionId){
        this.id = sessionId;
    }

    public HttpSession(){
        new HttpSession(UUID.randomUUID().toString());
    }

    public String getId() {
        return id;
    }

    public void setAttribute(String name, Object value){
        this.attributes.put(name, value);
    }

    public Object getAttribute(String name){
        return this.attributes.get(name);
    }

    public void removeAttribute(String name){
        this.attributes.remove(name);
    }

    public void invalidate(){
        HttpSessionUtils.remove(this.id);
    }
}
