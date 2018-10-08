package org.jenkinsci.plugins;

public class SimpleNotificationEvent extends NotificationEvent {
    public SimpleNotificationEvent() {
        this.text = "";
        this.sender = new Sender();
    }
    public String text;
    public Sender sender;

    public void updateStatus(String status){
        if( status == "success" ){
            this.text += " was successful.";
        } else if( status == "failed" ){
            this.text += " failed!";
        } else if( status == "started" ){
            this.text += " has started.";
        }
    }
}