package org.jenkinsci.plugins;

public class CardNotificationEvent extends NotificationEvent {
    public CardNotificationEvent() {
        this.cards[0] = new CardNotification();
        this.sender = new Sender();
    }
    public CardNotification[] cards;
    public Sender sender;

    public void updateStatus(String status){}
}
