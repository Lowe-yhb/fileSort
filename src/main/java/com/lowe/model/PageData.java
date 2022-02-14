package com.lowe.model;

import java.util.StringJoiner;

/**
 * Created on 2022/2/12 18:00
 *
 * @author Lowe Yang
 */
public class PageData {
    public PageData(String id, String url, String date) {
        this.id = id;
        this.url = url;
        this.date = date;
    }

    //用户id
    private String id;

    //页面地址
    private String url;

    //日期
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PageData.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("url='" + url + "'")
                .add("date='" + date + "'")
                .toString();
    }
}
