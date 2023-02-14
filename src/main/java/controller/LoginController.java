package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController{
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = DataBase.findUserById(userId);
        if(user == null) {
            response.forward("/user/login_failed.html");
            return;
        }
        if(user.getPassword().equals(password))
            response.sendRedirect("/index.html");
        else
            response.forward("/user/login_failed.html");
    }
}
