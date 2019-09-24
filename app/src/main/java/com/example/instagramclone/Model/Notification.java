package com.example.instagramclone.Model;

public class Notification {

    private String userid;
    private String text;
    private String postid;
    private boolean ispost;


    public Notification(String userid, String text, String postid, boolean ispost) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.ispost = ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }

    public boolean isIspost(){
        return ispost;

    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostid() {
        return postid;
    }

    public String getText() {
        return text;
    }

    public String getUserid() {
        return userid;
    }



}
