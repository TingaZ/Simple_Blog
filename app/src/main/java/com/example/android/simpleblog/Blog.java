package com.example.android.simpleblog;

/**
 * Created by Zack on 2016/11/28.
 */
public class Blog {

    String title, desc, image;

    public Blog() {
    }

    public Blog(String desc, String image, String title) {
        this.desc = desc;
        this.image = image;
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
