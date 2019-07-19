package com.tw.interview.homework.ssmanage.service;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;
import com.tw.interview.homework.ssmanage.service.impl.SessionTalkTrackServiceImpl;
import com.tw.interview.homework.ssmanage.strategies.SessionTalkTextParser;
import com.tw.interview.homework.ssmanage.strategies.impl.SessionTalkTextParserImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * test the track service
 */
public class TestSessionTalkTrackService {
    /**
     * test tract logic
     */
    @Test
    public void testTrackResult(){
        SessionTalkTrackService trackService = new SessionTalkTrackServiceImpl();
        SessionTalkTextParser parser = new SessionTalkTextParserImpl();
        List<Talk> talkList = parser.parse(new File("sample_data"));
        Assert.assertNotNull(talkList);
        Assert.assertTrue(!talkList.isEmpty());
        List<TalkTrackResult> results = trackService.trackTalk(talkList, 50);
        Assert.assertNotNull(results);
        Assert.assertTrue(!results.isEmpty());
        trackService.printResults(results);
    }
}
