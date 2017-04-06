package com.example.litan.myapplication;

/**
 * Created by litan on 2017/4/6.
 */

public class tcpClient extends Thread {
    String commandstring;

    public tcpClient(){
        commandstring = "1s";
    }
    public tcpClient(String command){
        commandstring = command;
    }

}
