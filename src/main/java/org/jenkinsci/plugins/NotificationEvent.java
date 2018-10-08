package org.jenkinsci.plugins;

abstract class NotificationEvent {
    abstract public void updateStatus(String status);
}
