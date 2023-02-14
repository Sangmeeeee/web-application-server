package controller;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ListUserController extends AbstractController{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if(!isLogin(request))
            response.forward("/user/login.html");
        else{
            String userTable = createUserTable();
            response.forwardBody(userTable);
        }
    }

    private boolean isLogin(HttpRequest request){
        String cookieHeader = request.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieHeader);
        String value = cookies.get("logined");
        if(Objects.isNull(value))
            return false;
        return Boolean.parseBoolean(value);
    }

    private String createUserTable(){
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        for(User user : users){
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
