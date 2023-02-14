package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class CreateUserController extends AbstractController{
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(request.getParam("userId"), request.getParam("password"), request.getParam("name"), request.getParam("email"));
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
