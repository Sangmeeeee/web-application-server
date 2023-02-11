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
    }

    @Test
    public void url(){
        String line = "GET /index.html HTTP/1.1";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
        assertThat(header.getRequestPath(), is("/index.html"));
    }

    @Test(expected = HeaderException.class)
    public void headerException(){
        String line = "";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
    }

    @Test
    public void parseRequestPath(){
        String line = "GET /user/create?userId=id&password=pw&name=nm&email=em HTTP/1.1";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
        assertThat(header.getParams().get("userId"), is("id"));
        assertThat(header.getParams().get("password"), is("pw"));
        assertThat(header.getParams().get("name"), is("nm"));
        assertThat(header.getParams().get("email"), is("em"));
    }
}