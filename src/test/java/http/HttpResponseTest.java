package http;



import org.junit.Test;

import java.io.*;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class HttpResponseTest {

    private final String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        response.forward("/index.html");
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        assertEquals(true, fileContainsString("Http_Forward.txt", "Content-Length: " + body.length));
    }

    @Test
    public void responseCSS() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_CSS.txt"));
        response.forward("/css/styles.css");
        byte[] body = Files.readAllBytes(new File("./webapp/css/styles.css").toPath());
        assertEquals(true, fileContainsString("Http_CSS.txt", "Content-Length: " + body.length));
    }

    @Test
    public void responseRedirect() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        response.sendRedirect("/index.html");
        assertEquals(true, fileContainsString("Http_Redirect.txt", "HTTP/1.1 302 Redirect"));
    }

    @Test
    public void responseCookie() throws Exception{
        HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
        assertEquals(true, fileContainsString("Http_Cookie.txt", "Set-Cookie: logined=true"));
    }

    private OutputStream createOutputStream(String fileName) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + fileName));
    }

    private boolean fileContainsString(String fileName, String str) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(testDirectory + fileName));
        String line = br.readLine();
        if(line.contains(str))
            return true;

        if(line == null)
            return false;
        while (!"".equals(line)){
            line = br.readLine();
            if(line.contains(str))
                return true;
        }
        return false;
    }
}