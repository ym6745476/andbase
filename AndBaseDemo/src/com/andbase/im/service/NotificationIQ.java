package com.andbase.im.service;

import org.jivesoftware.smack.packet.IQ;

import android.util.Log;

public class NotificationIQ extends IQ {

    private String id;

    private String apiKey;

    private String title;

    private String message;

    private String uri;

    public NotificationIQ() {
    }

    @Override
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("notification").append(" xmlns=\"").append(
                "androidpn:iq:notification").append("\">");
        if (id != null) {
            buf.append("<id>").append(id).append("</id>");
        }
        buf.append("</").append("notification").append("> ");
        return buf.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
    	Log.d("uri", uri);
        return uri;
    }

    public void setUri(String url) {
        this.uri = url;
    }

}
