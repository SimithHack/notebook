package com.tw.interview.homework.ssmanage.domain;

/**
 * the session (am,pm) talk
 */
public class Talk {
    /**
     * talk's title
     */
    private String title;
    /**
     * talk's time taking (in minutes)
     */
    private Integer timeTakeInMin;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTimeTakeInMin() {
        return timeTakeInMin;
    }

    public void setTimeTakeInMin(Integer timeTakeInMin) {
        this.timeTakeInMin = timeTakeInMin;
    }
}
