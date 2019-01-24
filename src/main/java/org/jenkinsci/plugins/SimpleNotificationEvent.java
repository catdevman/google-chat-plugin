package org.jenkinsci.plugins;

public class SimpleNotificationEvent extends NotificationEvent {
    public SimpleNotificationEvent() {
        this.text = "";
        this.sender = new Sender();
    }
    public String text;
    public Sender sender;
}