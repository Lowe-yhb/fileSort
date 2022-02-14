package com.lowe.model;

import java.util.StringJoiner;

/**
 * Created on 2022/2/12 18:00
 *
 * @author Lowe Yang
 */
public class UrlCount {
    public UrlCount(String url, Integer count) {
        this.url = url;
        this.count = count;
    }

    //页面地址
    private String url;

    //访问次数
    private Integer count;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", UrlCount.class.getSimpleName() + "[", "]")
                .add("url='" + url + "'")
                .add("count=" + count)
                .toString();
    }

}
