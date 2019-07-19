package com.tw.interview.homework.ssmanage.strategies;

import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;

/**
 * track condition
 * to implement all the requirement that the problem gives
 */
public interface TrackCondition extends Comparable<TrackCondition>{

    /**
     * the priority (the smaller the least priority)
     * @return Integer present condition's priority
     */
    default Integer priority(){
        return Integer.MIN_VALUE;
    }

    /**
     * whether this result is meet this condition
     * @param result the possible talk arrangement list
     * @return
     */
    boolean isReasonable(TalkTrackResult result);

    /**
     * implement the comparable interface to sort many condition
     * @param condition other condition that will be compared
     * @return which is more priority than others
     */
    @Override
    default int compareTo(TrackCondition condition) {
        return condition.priority().compareTo(priority());
    }
}
