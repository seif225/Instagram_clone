package com.example.instagramclone.Model;

public class User {

    private String id,username,fullname,imageurl,bio;

    public User(String id, String username, String fullname, String imageurl, String bio) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public User(){}

    public String getFullName() {
        return fullname;
    }

    public String getBio() {
        return bio;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public String getUserName() {
        return username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageurl = imageUrl;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

}
