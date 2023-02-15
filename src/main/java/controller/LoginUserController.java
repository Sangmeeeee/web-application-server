package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import session.HttpSession;

public class LoginUserController extends AbstractController{
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParam("userId"));
        if(user != null && user.getPassword().equals(request.getParam("password"))){
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/index.html");
        }else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
