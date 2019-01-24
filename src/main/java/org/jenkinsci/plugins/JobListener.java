package org.jenkinsci.plugins;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import com.alibaba.fastjson.JSON;
import okhttp3.*;

import javax.annotation.Nonnull;

import java.util.logging.Logger;
import java.util.logging.Level;

@Extension
public class JobListener extends RunListener<AbstractBuild>{

    transient final Logger logger = Logger.getLogger("org.jenkinsci.plugins");
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public JobListener() {
        super(AbstractBuild.class);
        client = new OkHttpClient();
    }

    @Override
    public void onStarted(AbstractBuild build, TaskListener listener) {
        GoogleChatBotPublisher publisher = GetWebHookPublisher(build);
        if (publisher == null || !publisher.onStart) {
            return;
        }
        String webHookUrl = publisher.webHookUrl;
        NotificationEvent event = this.generateEvent(publisher.startMessage, build);
        httpPost(webHookUrl, event);
    }

    @Override
    public void onCompleted(AbstractBuild build, @Nonnull TaskListener listener) {
        GoogleChatBotPublisher publisher = GetWebHookPublisher(build);
        if (publisher == null) {
            return;
        }
        Result result = build.getResult();
        if (result == null) {
            return;
        }
        String webHookUrl = publisher.webHookUrl;
        NotificationEvent event = this.generateEvent(publisher.messageType, build);
        if (publisher.onSuccess && result.equals(Result.SUCCESS)) {
            NotificationEvent event = this.generateEvent(publisher.successMessage, build);
        }
        if (publisher.onFailure && result.equals(Result.FAILURE)) {
            NotificationEvent event = this.generateEvent(publisher.failMessage, build);
        }
        
        httpPost(webHookUrl, event);
    }

    private GoogleChatBotPublisher GetWebHookPublisher(AbstractBuild build) {
        for (Object publisher : build.getProject().getPublishersList().toMap().values()) {
            if (publisher instanceof GoogleChatBotPublisher) {
                return (GoogleChatBotPublisher) publisher;
            }
        }
        return null;
    }

    private NotificationEvent generateEvent(String messageType, AbstractBuild build){
        String buildUrl = build.getAbsoluteUrl();
        String projectName = build.getProject().getDisplayName();
        String buildName = build.getDisplayName();
        SimpleNotificationEvent simpleNotificationEvent = new SimpleNotificationEvent();
        simpleNotificationEvent.text = projectName + ":" + buildName + " @ " + buildUrl;
        return simpleNotificationEvent;
       
    }
    private void httpPost(String url, Object object) {
        String jsonString = JSON.toJSONString(object);
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonString);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            response.close();
        }
    }
}
