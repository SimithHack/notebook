package com.tw.interview.homework.ssmanage.service;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.strategies.SessionTalkTextParser;
import com.tw.interview.homework.ssmanage.strategies.impl.SessionTalkTextParserImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

/**
 * test text parser
 */
public class TestSeesionTalkTextParser {
    /**
     * test file as input source
     */
    @Test
    public void testFile(){
        SessionTalkTextParser parser = new SessionTalkTextParserImpl();
        List<Talk> talkList = parser.parse(new File("sample_data"));
        Assert.assertNotNull(talkList);
        Assert.assertTrue(!talkList.isEmpty());
    }
    /**
     * test InputStream as input source
     */
    @Test
    public void testInputStream(){
        ByteArrayInputStream testDataInputStream = new ByteArrayInputStream(
                ("Writing Fast Tests Against Enterprise Rails 60min\n" +
                "Overdoing it in Python 45min\n" +
                "Lua for the Masses 30min\n" +
                "Ruby Errors from Mismatched Gem Versions 45min\n" +
                "Common Ruby Errors 45min\n" +
                "Rails for Python Developers lightning\n" +
                "Communicating Over Distance 60min\n" +
                "Accounting-Driven Development 45min\n" +
                "Woah 30min\n" +
                "Sit Down and Write 30min\n" +
                "Pair Programming vs Noise 45min\n" +
                "Rails Magic 70min\n" +
                "Ruby on Rails: Why We Should Move On 60min\n" +
                "Clojure Ate Scala (on my project) 25min\n" +
                "Programming in the Boondocks of Seattle lightning\n" +
                "Ruby vs. Clojure for Back-End Development 30min\n" +
                "Ruby on Rails Legacy App Maintenance 60min\n" +
                "A World Without HackerNews 30min\n" +
                "User Interface CSS in Rails Apps 30min").getBytes()
        );
        SessionTalkTextParser parser = new SessionTalkTextParserImpl();
        List<Talk> talkList = parser.parse(testDataInputStream);
        Assert.assertNotNull(talkList);
        Assert.assertTrue(!talkList.isEmpty());
    }

    /**
     * test string as input source
     */
    @Test
    public void testStr(){
        SessionTalkTextParser parser = new SessionTalkTextParserImpl();
        List<Talk> talkList = parser.parse("Writing Fast Tests Against Enterprise Rails 60min\n" +
                "Overdoing it in Python 45min\n" +
                "Lua for the Masses 30min\n" +
                "Ruby Errors from Mismatched Gem Versions 45min\n" +
                "Common Ruby Errors 45min\n" +
                "Rails for Python Developers lightning\n" +
                "Communicating Over Distance 60min\n" +
                "Accounting-Driven Development 45min\n" +
                "Woah 30min\n" +
                "Sit Down and Write 30min\n" +
                "Pair Programming vs Noise 45min\n" +
                "Rails Magic 60min\n" +
                "Ruby on Rails: Why We Should Move On 60min\n" +
                "Clojure Ate Scala (on my project) 45min\n" +
                "Programming in the Boondocks of Seattle 30min\n" +
                "Ruby vs. Clojure for Back-End Development 30min\n" +
                "Ruby on Rails Legacy App Maintenance 60min\n" +
                "A World Without HackerNews 30min\n" +
                "User Interface CSS in Rails Apps 30min");
        Assert.assertNotNull(talkList);
        Assert.assertTrue(!talkList.isEmpty());
    }
}
