package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream out){
        this.dos = new DataOutputStream(out);
    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

    public void forward(String url){
        try{
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            if(url.endsWith(".css"))
                headers.put("Content-Type", "text/css");
            else if(url.endsWith(".js"))
                headers.put("Content-Type", "application/javascript");
            else
                headers.put("Content-Type", "text/html;charset=utf-8");
            headers.put("Content-Length", String.valueOf(body.length));
            response200Header(body.length);
            responseBody(body);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String body){
        byte[] bytes = body.getBytes();
        addHeader("Content-Type", "text/html;charset=utf-8");
        addHeader("Content-Length", String.valueOf(bytes.length));
        response200Header(bytes.length);
        responseBody(bytes);
    }

    public void sendRedirect(String redirectUrl){
        try{
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            processHeaders();
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
            dos.writeBytes("\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void response200Header(int length) {
        try{
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try{
            dos.write(body, 0, body.length);
            dos.flush();
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void processHeaders() {
        try {
            for (String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) + " \r\n");
            }
        }catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
