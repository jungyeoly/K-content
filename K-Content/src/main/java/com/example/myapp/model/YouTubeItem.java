package com.example.myapp.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YouTubeItem {
    private String url;
    private String title;
    private String thumbnail;
    private String description;

    public YouTubeItem(String url, String title, String thumbnail, String description) {
        this.url = url;
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
    }


}
