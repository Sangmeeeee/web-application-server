package http;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestLineTest {
    @Test
    public void create_GET_method(){
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertEquals(HttpMethod.GET, line.getMethod());
    }

    @Test
    public void create_POST_method(){
        RequestLine line = new RequestLine("POST /index.html HTTP/1.1");
        assertEquals(HttpMethod.POST, line.getMethod());
    }

    @Test
    public void create_GET_path_and_params(){
        RequestLine line = new RequestLine("GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1");
        assertEquals("javajigi", line.getParam().get("userId"));
    }
}