package http;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpCookie {
    private Map<String, String> cookies;

    public HttpCookie(String cookieLine){
        this.cookies = HttpRequestUtils.parseCookies(cookieLine);
    }

    public String getCookie(String name){
        return this.cookies.get(name);
    }
}
