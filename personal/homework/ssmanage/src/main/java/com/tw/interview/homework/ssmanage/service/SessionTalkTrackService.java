package com.tw.interview.homework.ssmanage.service;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;

import java.util.List;

/**
 * the talk arrangement
 * arrange user's talk issues according to it's time taking
 */
public interface SessionTalkTrackService {
    /**
     * get the possible results
     * @param talks talk content
     * @param resultSize the result size user expected to return
     * @return possible results
     */
    List<TalkTrackResult> trackTalk(List<Talk> talks, int resultSize);

    /**
     * output the possible result to the user
     * @param results all possible result that satisfy all the conditions
     */
    void printResults(List<TalkTrackResult> results);
}
