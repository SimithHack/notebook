package com.tw.interview.homework.ssmanage.strategies.impl;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;
import com.tw.interview.homework.ssmanage.strategies.TrackCondition;

import java.util.List;

/**
 * whether the talk list is illegal for morning session
 * for these condition
 *  1) Morning sessions begin at 9am and must finish before 12 noon, for lunch.
 */
public class MorningSessionCondition implements TrackCondition {

    /**
     * total time in morning
     */
    private static final int TOTAL_DUR = 180;

    @Override
    public Integer priority() {
        return 10;
    }

    @Override
    public boolean isReasonable(TalkTrackResult result) {
        //talk time take accelerator
        int sumAcc = 0;
        List<Talk> talkList = result.getTalks();
        for(int i=0; i < talkList.size(); i++){
            Talk talk = talkList.get(i);
            if((sumAcc + talk.getTimeTakeInMin()) > TOTAL_DUR){
                break;
            }else{
                sumAcc += talk.getTimeTakeInMin();
                result.setAmIncludeEndPos(i);
            }
        }
        return (sumAcc == TOTAL_DUR);
    }
}
