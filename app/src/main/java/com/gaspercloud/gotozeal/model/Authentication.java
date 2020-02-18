package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Authentication implements Serializable {

    @SerializedName("app_name")
    private String app_name;
    @SerializedName("scope")
    private String scope;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("return_url")
    private String return_url;
    @SerializedName("callback_url")
    private String callback_url;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "app_name='" + app_name + '\'' +
                ", scope='" + scope + '\'' +
                ", user_id='" + user_id + '\'' +
                ", return_url='" + return_url + '\'' +
                ", callback_url='" + callback_url + '\'' +
                '}';
    }

    public class AuthenticationResponse{
        @SerializedName("key_id")
        private String key_id;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("consumer_key")
        private String consumer_key;
        @SerializedName("consumer_secret")
        private String consumer_secret;
        @SerializedName("key_permissions")
        private String key_permissions;

        public String getKey_id() {
            return key_id;
        }

        public void setKey_id(String key_id) {
            this.key_id = key_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getConsumer_key() {
            return consumer_key;
        }

        public void setConsumer_key(String consumer_key) {
            this.consumer_key = consumer_key;
        }

        public String getConsumer_secret() {
            return consumer_secret;
        }

        public void setConsumer_secret(String consumer_secret) {
            this.consumer_secret = consumer_secret;
        }

        public String getKey_permissions() {
            return key_permissions;
        }

        public void setKey_permissions(String key_permissions) {
            this.key_permissions = key_permissions;
        }

        @Override
        public String toString() {
            return "AuthenticationResponse{" +
                    "key_id='" + key_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", consumer_key='" + consumer_key + '\'' +
                    ", consumer_secret='" + consumer_secret + '\'' +
                    ", key_permissions='" + key_permissions + '\'' +
                    '}';
        }
    }
}
