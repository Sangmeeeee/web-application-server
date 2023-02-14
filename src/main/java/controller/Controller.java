package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
    public void service(HttpRequest request, HttpResponse response);
}
