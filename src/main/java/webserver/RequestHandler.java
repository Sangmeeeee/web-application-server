package webserver;

import controller.Controller;
import http.HttpCookie;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.HttpSession;
import util.HttpSessionUtils;
import util.HttpRequestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

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
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            String sessionId = request.getCookies().getCookie("JSESSIONID");
            if(Objects.isNull(sessionId)){
                HttpSession session = new HttpSession();
                response.addHeader("Set-Cookie", "JSESSIONID=" + session.getId());
                HttpSessionUtils.addSession(session);
            }

            Controller controller = RequestMapping.getController(request.getPath());
            if(controller == null){
                response.forward(getDefaultPath(request.getPath()));
            }else {
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path){
        if(path.equals("/"))
            return "/index.html";
        return path;
    }
}
