package com.example.phatnguyen.facebookapidemo.Activity.Model;

/**
 * Created by phatnguyen on 10/19/17.
 */

public class FaceBookUser {
    private String name;
    private String user_id;
    private String url_image;
    public FaceBookUser(String name,String user_id,String url_image) {
        this.name=name;
        this.user_id=user_id;
        this.url_image=url_image;
    }
    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
