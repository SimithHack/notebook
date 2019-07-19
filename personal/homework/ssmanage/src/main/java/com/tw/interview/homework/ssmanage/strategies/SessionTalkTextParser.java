package com.tw.interview.homework.ssmanage.strategies;

import com.tw.interview.homework.ssmanage.domain.Talk;
import com.tw.interview.homework.ssmanage.exceptions.SSMangeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * parse user's input into talks
 */
public interface SessionTalkTextParser {
    /**
     * parse the input string
     * @param ipt input string
     * @return talk list
     */
    List<Talk> parse(String ipt);

    /**
     * parse from Inputstream
     * @param iptStream inputstream
     * @return talk list
     */
    default List<Talk> parse(InputStream iptStream){
        byte[] data = new byte[1024];
        ByteArrayOutputStream memOs = new ByteArrayOutputStream();
        //read content from input stream
        try {
            int pos;
            while((pos=iptStream.read(data)) > 0){
                memOs.write(data, 0, pos);
            }
            return parse(new String(memOs.toByteArray(), "utf-8"));
        } catch (IOException e) {
            return new ArrayList(0);
        }
    }

    /**
     * from file to parse
     * @param iptFile file that contains talks
     * @return talk list
     */
    default List<Talk> parse(File iptFile){
        try {
            return parse(new FileInputStream(iptFile));
        } catch (FileNotFoundException e) {
            throw new SSMangeException(String.format("file %s doesn't existed on disk", iptFile.getPath()));
        }
    }
}
