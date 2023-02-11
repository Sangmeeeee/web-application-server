package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            Header header = new Header(br);
            
            if("GET".equals(header.getMethod())){
                if(header.getRequestPath().endsWith(".css")){
                    byte[] body = Files.readAllBytes(new File("./webapp/" + header.getRequestPath()).toPath());
                    responseCssHeader(dos, body.length);
                    responseBody(dos, body);
                } else if("/user/list".equals(header.getRequestPath())){
                    if(Objects.isNull(header.getCookies().get("logined")) || !Boolean.parseBoolean(header.getCookies().get("logined"))){
                        response302Header(dos, "/index.html");
                    }else{
                        byte[] body = responseUserList();
                        response200Header(dos, body.length);
                        responseBody(dos, body);
                    }
                }else {
                    byte[] body = Files.readAllBytes(new File("./webapp" + header.getRequestPath()).toPath());
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            }else if("POST".equals(header.getMethod())){
                if("/user/create".equals(header.getRequestPath())){
                    User user = new User(header);
                    DataBase.addUser(user);
                    response302Header(dos, "/index.html");
                }else if("/user/login".equals(header.getRequestPath())){
                    String userId = header.getParams().get("userId");
                    String password = header.getParams().get("password");
                    User user = DataBase.findUserById(userId);
                    Map<String, String> cookies = new HashMap<>();
                    if(Objects.isNull(user) || !password.equals(user.getPassword())){
                        cookies.put("test", "a");
                        response302Header(dos, "/user/login_failed.html", cookies);
                    }else{
                        cookies.put("logined", "true");
                        response302Header(dos, "/index.html", cookies);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseCssHeader(DataOutputStream dos, int lengthOfBodyContent){
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location){
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.flush();
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location, Map<String, String> cookies){
        StringBuilder sb = new StringBuilder();
        for(String key : cookies.keySet())
            sb.append(key + "=" + cookies.get(key) + "; ");

        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Set-Cookie: " + sb.toString());
            dos.flush();
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] responseUserList(){
        StringBuilder sb = new StringBuilder();
        DataBase.findAll().forEach(user -> {
            sb.append(user.getUserId() + ", "  + user.getName());
        });
        return sb.toString().getBytes();
    }
}
