package webserver;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HeaderTest{
    @Test
    public void getHeader(){
        String line = "GET /index.html HTTP/1.1";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
        assertThat(header.getMethod(), is("GET"));
        assertThat(header.getRequestPath(), is("/index.html"));
    }

    @Test(expected = HeaderException.class)
    public void headerException(){
        String line = "";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
    }

    @Test
    public void parseRequestParam(){
        String line = "GET /user/create?userId=id&password=pw&name=nm&email=em HTTP/1.1";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
        assertThat(header.getParams().get("userId"), is("id"));
        assertThat(header.getParams().get("password"), is("pw"));
        assertThat(header.getParams().get("name"), is("nm"));
        assertThat(header.getParams().get("email"), is("em"));
    }

    @Test
    public void postHeader(){
        StringBuilder sb = new StringBuilder();
        sb.append("POST /user/create HTTP/1.1\r\n");
        sb.append("Content-Length: 59\r\n");
        sb.append("\r\n");
        sb.append("userId=javajigi&password=password&name=JaeSung\r\n");
        BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
        Header header = new Header(br);
        assertThat(header.getMethod(), is("POST"));
        assertThat(header.getRequestPath(), is("/user/create"));
    }

    @Test
    public void parsePostBody(){
        StringBuilder sb = new StringBuilder();
        sb.append("POST /user/create HTTP/1.1\r\n");
        sb.append("Content-Length: 59\r\n");
        sb.append("\r\n");
        sb.append("userId=javajigi&password=password&name=JaeSung\r\n");
        BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
        Header header = new Header(br);
        assertThat(header.getParams().get("userId"), is("javajigi"));
        assertThat(header.getParams().get("password"), is("password"));
        assertThat(header.getParams().get("name"), is("JaeSung"));
    }
}