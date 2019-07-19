package com.tw.interview.homework.ssmanage.strategies.impl;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;
import com.tw.interview.homework.ssmanage.strategies.TrackCondition;

import java.util.List;

/**
 * afternoon session condition
 * must meet these limits
 *  1) Afternoon sessions begin at 1pm  must finish in time for the networking event.
 *  2) The networking event can start no earlier than 4:00 and no later than 5:00.
 */
public class AfternoonSessionCondition implements TrackCondition {
    /**
     * total time in afternoon
     */
    private static final Integer TOTAL_DUR = 240;

    @Override
    public boolean isReasonable(TalkTrackResult result) {
        List<Talk> talkList = result.getTalks();
        //talk time take accelerator
        int sumAcc = 0;
        for(int i=result.getAmIncludeEndPos()+1; i < talkList.size(); i++){
            Talk talk = talkList.get(i);
            sumAcc += talk.getTimeTakeInMin();
            if(sumAcc <= TOTAL_DUR){
                result.setPmIncludeEndPos(i);
            }else{
                break;
            }
        }
        return result.getAmIncludeEndPos()<result.getPmIncludeEndPos();
    }
}
