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
        assertThat(header.getUrl(), is("/index.html"));
    }

    @Test(expected = HeaderException.class)
    public void headerException(){
        String line = "";
        BufferedReader br = new BufferedReader(new StringReader(line));
        Header header = new Header(br);
    }
}