package webserver;

import lombok.Getter;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.util.Map;

@Getter
public class Header {
    String method;
    String requestPath;
    Map<String, String> params;

    public Header(BufferedReader br) throws HeaderException {
        try {
            String line = br.readLine();
            String[] token = line.split(" ");
            String url = token[1];
            this.method = token[0];

            int index = url.indexOf("?");
            if(index != -1) {
                this.requestPath = url.substring(0, index);
                params = HttpRequestUtils.parseQueryString(url.substring(index + 1));
            }else
                this.requestPath = url;

            while (line != null && !"".equals(line))
                line = br.readLine();
        } catch (Exception e) {
            throw new HeaderException();
        }
    }
}
