package webserver;

import lombok.Getter;

import java.io.BufferedReader;

@Getter
public class Header {
    String method;
    String url;

    public Header(BufferedReader br) throws HeaderException {
        try {
            String line = br.readLine();
            String[] token = line.split(" ");
            this.method = token[0];
            this.url = token[1];
            while (line != null && !"".equals(line))
                line = br.readLine();
        } catch (Exception e) {
            throw new HeaderException();
        }
    }
}
