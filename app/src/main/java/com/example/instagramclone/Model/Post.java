package com.example.instagramclone.Model;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;

    public Post(String postid, String postimage, String description, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getPostid() {
        return postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public String getPublisher() {
        return publisher;
    }
    public Post(){}
}





