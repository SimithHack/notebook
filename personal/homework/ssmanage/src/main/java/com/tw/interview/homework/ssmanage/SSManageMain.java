package com.tw.interview.homework.ssmanage;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.domain.TalkTrackResult;
import com.tw.interview.homework.ssmanage.exceptions.SSMangeException;
import com.tw.interview.homework.ssmanage.service.SessionTalkTrackService;
import com.tw.interview.homework.ssmanage.service.impl.SessionTalkTrackServiceImpl;
import com.tw.interview.homework.ssmanage.strategies.SessionTalkTextParser;
import com.tw.interview.homework.ssmanage.strategies.impl.SessionTalkTextParserImpl;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * the main entry point of SSManage
 */
public class SSManageMain {
    /**
     * the default sample_data path
     */
    public final static String DEFAULT_PATH = "sample_data";
    /**
     * exit flag
     */
    public final static String EXIT_FLAG = "exit";

    public static void main(String[] args){
        //read user input
        Scanner scanner = new Scanner(System.in);
        //the input file
        File iptFile = null;
        //whether we keep on reading user's input
        boolean keeponFlag = true;
        //make sure the rightness of user's input
        while(keeponFlag){
            System.out.print("Please input your task list path (default use 'sample_data' or 'exit' to exit): ");
            String filePath = scanner.nextLine();
            //empty input we use default path for example
            if(null==filePath || "".equals(filePath.trim())){
                filePath = DEFAULT_PATH;
                keeponFlag = false;
            }
            //user give up
            if(EXIT_FLAG.equalsIgnoreCase(filePath)){
                return;
            }
            //valid the file is existed on disk
            iptFile = new File(filePath);
            if(!iptFile.exists()){
                System.out.println("the file doesn't existed on disk , your current directory is : " + System.getProperty("user.dir"));
            }else{
                keeponFlag = false;
            }
        }
        System.out.print("How many result do you want to return ? (default 5): ");
        int resultSize = scanner.nextInt();
        //we can use IOC framework like (spring) to help us manage theses beans
        SessionTalkTextParser parser = new SessionTalkTextParserImpl();
        SessionTalkTrackService trackService = new SessionTalkTrackServiceImpl();
        //parse user's input
        List<Talk> talkList = parser.parse(iptFile);
        //we get the possible results
        try {
            List<TalkTrackResult> results = trackService.trackTalk(talkList, resultSize);
            //output the result to user
            trackService.printResults(results);
        } catch (SSMangeException e){
            System.out.println(e.getMessage());
        }
    }
}
