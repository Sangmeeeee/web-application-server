package webserver;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Header {
    private static final Logger log = LoggerFactory.getLogger(Header.class);
    private String method;
    private String requestPath;
    private Map<String, String> params;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies;

    public Header(BufferedReader br) {
        try {
            parseFirstLine(br);
            parseRequest(br);
            parsePostBody(br);
        } catch (Exception e) {
            throw new HeaderException();
        }
    }

    private void parseFirstLine(BufferedReader br) throws IOException {
        String[] token = br.readLine().split(" ");
        this.method = token[0];
        this.requestPath = token[1];
        if("GET".equals(this.method)){
            int index = this.requestPath.indexOf("?");
            if(index != -1) {
                params = HttpRequestUtils.parseQueryString(this.requestPath.substring(index + 1));
                this.requestPath = this.requestPath.substring(0, index);
            }
        }
    }

    private void parseRequest(BufferedReader br) throws IOException{
        int index;
        String line;
        while (true) {
            line = br.readLine();
            if("".equals(line) || line == null) break;
            index = line.indexOf(":");
            String key = line.substring(0, index);
            String value = line.substring(index + 1).trim();
            if("Cookie".equals(key)){
                cookies = HttpRequestUtils.parseCookies(value);
            }else
                headers.put(key, value);
        }
    }

    private void parsePostBody(BufferedReader br) throws IOException{
        if("POST".equals(this.method)){
            String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            params = HttpRequestUtils.parseQueryString(query);
        }
    }
}
