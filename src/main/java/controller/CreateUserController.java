package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class CreateUserController extends AbstractController{
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = createUser(request);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }

    private User createUser(HttpRequest request){
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        return new User(userId, password, name, email);
    }
}
