package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    public HttpRequest(InputStream in) {
        this.headers = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))){
            String line = br.readLine();
            if(Objects.isNull(line))
                return;
            parseRequestLine(line);
            parseRequestHeader(br, line);
            if(isPostMethod())
                parseBody(br);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void parseRequestLine(String requestLine){
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        this.path = tokens[1];
        if("GET".equals(method)){
            int index = this.path.indexOf("?");
            if(index != -1){
                this.parameters = HttpRequestUtils.parseQueryString(this.path.substring(index + 1));
                this.path = this.path.substring(0, index);
            }
        }
    }

    private void parseRequestHeader(BufferedReader br, String line) throws IOException {
        while (!"".equals(line)){
            line = br.readLine();
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            if(!Objects.isNull(pair))
                this.headers.put(pair.getKey(), pair.getValue());
        }
    }

    private boolean isPostMethod(){
        return "POST".equals(this.method);
    }

    private void parseBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(getHeader("Content-Length"));
        String body = IOUtils.readData(br, contentLength);
        this.parameters = HttpRequestUtils.parseQueryString(body);
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getParameter(String key) {
        return this.parameters.get(key);
    }
}
