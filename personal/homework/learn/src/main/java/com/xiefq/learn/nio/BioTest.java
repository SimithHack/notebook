package com.xiefq.learn.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class BioTest {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(1000);
        while (true){
            Socket s = ss.accept();
            new Thread(()->{
                try {
                    TimeUnit.SECONDS.sleep(1);
                    s.getOutputStream().write("you get a response from 9527 server".getBytes());
                    s.close();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
