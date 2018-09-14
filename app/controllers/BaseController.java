package controllers;

import play.mvc.Controller;

public class BaseController extends Controller {
    void addMessage(MessageType messageType, String message) {
        System.out.println(messageType.toString());
        flash().put(messageType.toString(), message);
    }

    public enum MessageType {
        ERROR,
        INFO,
        SUCCESS,
        WARNING
    }
}
