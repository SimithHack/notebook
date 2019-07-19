package com.tw.interview.homework.ssmanage.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * the result that we think it's reasonable
 */
public class TalkTrackResult {
    /**
     * the talk list , the order is import
     */
    private List<Talk> talks;
    /**
     *  the split position that separate am
     */
    private int amIncludeEndPos = -1;
    /**
     *  the split position that separate pm
     */
    private int pmIncludeEndPos = -1;

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = new ArrayList<>(talks);
    }

    public int getAmIncludeEndPos() {
        return amIncludeEndPos;
    }

    public void setAmIncludeEndPos(int amIncludeEndPos) {
        this.amIncludeEndPos = amIncludeEndPos;
    }

    public int getPmIncludeEndPos() {
        return pmIncludeEndPos;
    }

    public void setPmIncludeEndPos(int pmIncludeEndPos) {
        this.pmIncludeEndPos = pmIncludeEndPos;
    }
}
