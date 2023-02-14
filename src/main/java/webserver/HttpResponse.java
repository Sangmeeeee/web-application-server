package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos;
    private Map<String, String> headers;

    public HttpResponse(OutputStream out){
        headers = new HashMap<>();
        this.dos = new DataOutputStream(out);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            if(isCssResponse(url))
                responseCssHeader(body.length);
            else if(isJsResponse(url))
                responseJsHeader(body.length);
            else
                response200Header(body.length);
            responseBody(body);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String str) {
        try{
            byte[] body = str.getBytes();
            response200Header(body.length);
            responseBody(body);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String location) {
        try {
            response302Header(location);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private boolean isCssResponse(String url){
        return url.endsWith(".css");
    }

    private boolean isJsResponse(String url){
        return url.endsWith(".js");
    }

    private void responseCssHeader(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/css\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response200Header(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        addHeaderToResponse();
        dos.writeBytes("\r\n");
    }

    private void responseJsHeader(int lengthOfBodyContent) throws IOException{
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: application/javascript\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void addHeaderToResponse() throws IOException {
        for(String key : headers.keySet())
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
    }

    private void responseBody(byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.writeBytes("\r\n");
        dos.flush();
    }

    private void response302Header(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
        dos.writeBytes("Location: " + location + " \r\n");
        addHeaderToResponse();
        dos.writeBytes("\r\n");
    }
}
