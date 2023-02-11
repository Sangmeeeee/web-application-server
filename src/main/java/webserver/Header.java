package webserver;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Header {
    private static final Logger log = LoggerFactory.getLogger(Header.class);
    String method;
    String requestPath;
    Map<String, String> params;
    Map<String, String> headers;

    public Header(BufferedReader br) throws HeaderException {
        try {
            String line = br.readLine();
            String[] token = line.split(" ");
            String url = token[1];
            this.method = token[0];

            headers = new HashMap<>();
            while (true) {
                line = br.readLine();
                if("".equals(line) || line == null) break;
                int index = line.indexOf(":");
                headers.put(line.substring(0, index), line.substring(index + 1).trim());
            }

            if("POST".equals(this.method)){
                String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                params = HttpRequestUtils.parseQueryString(query);
            }else if("GET".equals(this.method)){
                int index = url.indexOf("?");
                if(index != -1) {
                    this.requestPath = url.substring(0, index);
                    params = HttpRequestUtils.parseQueryString(url.substring(index + 1));
                }else
                    this.requestPath = url;
            }
        } catch (Exception e) {
            throw new HeaderException();
        }
    }
}
