package org.jenkinsci.plugins;

import org.kohsuke.stapler.DataBoundConstructor;
import hudson.tasks.Notifier;
import hudson.tasks.BuildStepMonitor;
import hudson.Extension;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.model.AbstractProject;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import java.io.IOException;

public class GoogleChatBotPublisher extends Notifier {
    public String webHookUrl;
    public String messageType;
    public Boolean onStart;
    public Boolean onSuccess;
    public Boolean onFailure;


    @DataBoundConstructor
    public GoogleChatBotPublisher(String webHookUrl, boolean onStart, boolean onSuccess, boolean onFailure, String messageType) {
        super();
        this.webHookUrl = webHookUrl;
        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.messageType = messageType;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return true;
    }

    @Override
    public GoogleChatBotPublisherDescriptor getDescriptor() {
        return (GoogleChatBotPublisherDescriptor) super.getDescriptor();
    }

    @Extension
    public static class GoogleChatBotPublisherDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Google Chat WebHook Notification";
        }
    }
}
