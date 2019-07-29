package com.tw.interview.homework.ssmanage.service.impl;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;
import com.tw.interview.homework.ssmanage.service.SessionTalkTrackService;
import com.tw.interview.homework.ssmanage.strategies.TrackCondition;
import com.tw.interview.homework.ssmanage.strategies.impl.AfternoonSessionCondition;
import com.tw.interview.homework.ssmanage.strategies.impl.MorningSessionCondition;

import java.text.DecimalFormat;
import java.util.*;

/**
 * session manager's logical implement
 */
public class SessionTalkTrackServiceImpl implements SessionTalkTrackService {
    /**
     * condition list
     *  in IOC framework like spring , we can inject beans
     */
    private List<TrackCondition> conditions;
    {
        conditions = Arrays.asList(
                new MorningSessionCondition(),
                new AfternoonSessionCondition()
        );
    }
    @Override
    public List<TalkTrackResult> trackTalk(List<Talk> talks, int resultSize) {
        List<TalkTrackResult> results = new ArrayList<>();
        //sort by priority
        conditions.sort(TrackCondition::compareTo);
        //shuffle array while find an answer
        boolean findResult = false;
        //result counter
        while(!findResult){
            //generate a result randomly
            Collections.shuffle(talks);
            TalkTrackResult result = new TalkTrackResult();
            result.setTalks(talks);
            int conditionMeetCount = 0;
            //valid the result
            for(TrackCondition condition : conditions){
                if(condition.isReasonable(result)){
                    conditionMeetCount++;
                }else{
                    break;
                }
            }
            //all conditions must meet
            if(conditionMeetCount == conditions.size()){
                results.add(result);
            }
            //meet the size requirement
            if(resultSize == results.size()){
                findResult = true;
            }
        }
        return results;
    }

    @Override
    public void printResults(List<TalkTrackResult> results) {
        for(int i=0; i< results.size(); i++){
            System.out.println(String.format("Track %s", i+1));
            printResult(results.get(i));
        }
    }

    /**
     * print possible result
     * @param result the right result
     */
    private void printResult(TalkTrackResult result) {
        List<Talk> talks = result.getTalks();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        //print morning
        int amAcc = 0;
        for(int i = 0; i <= result.getAmIncludeEndPos(); i++){
            Talk talk = talks.get(i);
            System.out.println(String.format("%s:%s AM %s", decimalFormat.format(9+amAcc/60), decimalFormat.format(amAcc%60), talk.getTitle()));
            amAcc += talk.getTimeTakeInMin();
        }
        System.out.println("12:00 PM Lunch");
        //print afternoon
        int pmAcc = 0;
        for(int i = result.getAmIncludeEndPos()+1; i <= result.getPmIncludeEndPos(); i++){
            Talk talk = talks.get(i);
            System.out.println(String.format("%s:%s PM %s", decimalFormat.format(1+pmAcc/60), decimalFormat.format(pmAcc%60), talk.getTitle()));
            pmAcc += talk.getTimeTakeInMin();
        }
        System.out.println(String.format("%s:%s PM Networking Event", decimalFormat.format(1+pmAcc/60), decimalFormat.format(pmAcc%60)));
    }
}
