package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private OutputStream out;
    private Map<String, String> headers;

    public HttpResponse(OutputStream out){
        headers = new HashMap<>();
        this.out = out;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void forward(String url) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            if(isCssResponse(url))
                responseCssHeader(dos, body.length);
            else
                response200Header(dos, body.length);
            responseBody(dos, body);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private boolean isCssResponse(String url){
        return url.endsWith(".css");
    }

    private void responseCssHeader(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/css\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        addHeaderToResponse(dos);
        dos.writeBytes("\r\n");
    }

    private void addHeaderToResponse(DataOutputStream dos) throws IOException {
        for(String key : headers.keySet())
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void sendRedirect(String location) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            response302Header(dos, location);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
        dos.writeBytes("Location: " + location + " \r\n");
        addHeaderToResponse(dos);
        dos.writeBytes("\r\n");
    }
}
