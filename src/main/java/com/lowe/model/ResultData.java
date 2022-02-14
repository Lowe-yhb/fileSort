package com.lowe.model;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created on 2022/2/12 18:00
 *
 * @author Lowe Yang
 */
public class ResultData {

    //访问次数最多的前20的页面
    private List<UrlCount> top20Pages;

    //访问次数最多的前20的页面, 中的前20用户
    private Map<String, List<PageData>> top20PagesTopUsers;

    //访问次数最少的前20的页面
    private List<UrlCount> bottom20Pages;

    public List<UrlCount> getTop20Pages() {
        return top20Pages;
    }

    public void setTop20Pages(List<UrlCount> top20Pages) {
        this.top20Pages = top20Pages;
    }

    public Map<String, List<PageData>> getTop20PagesTopUsers() {
        return top20PagesTopUsers;
    }

    public void setTop20PagesTopUsers(Map<String, List<PageData>> top20PagesTopUsers) {
        this.top20PagesTopUsers = top20PagesTopUsers;
    }

    public List<UrlCount> getBottom20Pages() {
        return bottom20Pages;
    }

    public void setBottom20Pages(List<UrlCount> bottom20Pages) {
        this.bottom20Pages = bottom20Pages;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResultData.class.getSimpleName() + "[", "]")
                .add("top20Pages=" + top20Pages)
                .add("top20PagesTopUsers=" + top20PagesTopUsers)
                .add("bottom20Pages=" + bottom20Pages)
                .toString();
    }
}
