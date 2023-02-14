package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private HttpHeader headers = new HttpHeader();
    private Map<String, String> params = new HashMap<>();

    public HttpRequest(InputStream in){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if(line == null)
                return;
            requestLine = new RequestLine(line);

            line = br.readLine();
            while (!line.equals("")){
                log.debug("header : {}", line);
                headers.parseRequestHeader(line);
                line = br.readLine();
            }

            if(isPost()){
                String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                params = HttpRequestUtils.parseQueryString(body);
            }else
                params = requestLine.getParam();
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    private boolean isPost(){
        return getMethod().isPost();
    }
}