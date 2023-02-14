package http;

import java.util.HashMap;
import java.util.Map;

public class HttpHeader {
    private Map<String, String> headers = new HashMap<>();

    public void put(String key, String value){
        headers.put(key, value);
    }

    public String get(String key){
        return this.headers.get(key);
    }

    public void parseRequestHeader(String line){
        String[] tokens = line.split(": ");
        this.headers.put(tokens[0].trim(), tokens[1].trim());
    }
}
