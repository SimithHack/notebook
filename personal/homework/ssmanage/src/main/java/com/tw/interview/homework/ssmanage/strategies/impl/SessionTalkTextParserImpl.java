package com.tw.interview.homework.ssmanage.strategies.impl;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.exceptions.SSMangeException;
import com.tw.interview.homework.ssmanage.strategies.SessionTalkTextParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the implementation of SessionTalkTextParser
 */
public class SessionTalkTextParserImpl implements SessionTalkTextParser {
    /**
     * the pattern that match user's input
     */
    public static final Pattern TALK_PATTERN = Pattern.compile("[^\\d]*(((?<minute>\\d{1,3})min)|(?<lightning>lightning)).*", Pattern.CASE_INSENSITIVE);

    @Override
    public List<Talk> parse(String ipt) {
        //get each line
        String[] lines = ipt.split("\n");
        //the talks that we will parse from user's input
        List<Talk> talks = new ArrayList<>();
        for(String line : lines){
            //we ignore empty line
            if(!"".equals(line.trim())){
                talks.add(parseTalk(line.trim()));
            }
        }
        return talks;
    }

    /**
     * parse each line as Talk instance, we should obey the syntax
     * @param line
     * @return
     */
    private Talk parseTalk(String line) {
        Matcher matcher = TALK_PATTERN.matcher(line);
        if(matcher.matches()){
            Integer minute;
            //we capture talk's time taking in minute
            String minStr = matcher.group("minute");
            if(null != minStr && !"".equals(minStr)){
                minute = Integer.parseInt(minStr);
            }
            // the other cause must be lightning
            else{
                minute = 5;
            }
            //construct talk instance
            Talk talk = new Talk();
            talk.setTitle(line);
            talk.setTimeTakeInMin(minute);
            return talk;
        }else{
            throw new SSMangeException(String.format("\"%s\" is a corrupt input", line));
        }
    }
}
