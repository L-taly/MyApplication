package com.example.litan.myapplication;


import android.content.Context;
import android.net.IpPrefix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by litan on 2017/4/5.
 */

public class page extends Activity {

    //Default
    public static final String DEFAULT_IP = "192.168.199.136";
    public static final String PREFS_NAME = "PreferencesFile";
    public static final int CONNENTED = 0;
    public static final int UPDATALOG = 1;

    //Default Port
    private static final int PORT = 6000;

    private Button Close;
    private Button clean;
    private Button send;
    private TextView log;
    private EditText ipEdit;
    private EditText command;
    private String ip;
    private String logMsg;
    private Socket socket;
    private BufferedReader writer;
    private InetSocketAddress isa = null;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);
        mContext = this;
        findviews();

        //close the page
        Close = (Button) findViewById(R.id.btnClose);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout root=(LinearLayout) findViewById(R.id.linearlayout2);
        root.addView(new MyView(page.this));

    }
    public void findviews(){
        //Get all views
        command = (EditText) findViewById(R.id.command);
        ipEdit = (EditText) findViewById(R.id.ipEdit);
        clean = (Button) findViewById(R.id.clean);
        send = (Button) findViewById(R.id.send);
        log = (TextView) findViewById(R.id.log);
    }

    public void setonclick(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            //Button click, tcp start
            public void onClick(View v) {
                ip = ipEdit.getText().toString();
                tcpClient tcp = new tcpClient(command.getText().toString());
                tcp.start();
            }
        });
        clean.setOnClickListener(new Button.OnClickListener(){
            @Override
            //clean log
            public void onClick(View v){
                logMsg = "";
                log.setText(logMsg);
            }
        });
    }

    public void ConnectToServer() throws UnknownHostException, IOException{
        socket = RequestSocket(ip,PORT);
        if(socket.isConnected()){
            Message msg = new Message();
            msg.what = CONNENTED;
            mHandler.sendMessage(msg);
        }
    }

    //host:port
    public Socket RequestSocket(String host, int port) throws UnknownHostException, IOException{
        Socket ConSocket = new Socket();
        isa = new InetSocketAddress(host, port);
        //Set Connection
        ConSocket.connect(isa);
        return ConSocket;
    }

    //send message to server
    private void SendMsg(Socket socket, String msg) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Log.i("msg", msg.replace("\n", " ") + "\n");
        writer.write(msg.replace("\n", " ") + "\n");
    }

    //get message from server
    public String RevciveMessage()

    public class tcpClient extends Thread {
        String commandstring;

        public tcpClient(){
            commandstring = "1s";
        }
        public tcpClient(String command){
            commandstring = command;
        }

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNENTED:
                    logMsg += "Server Connected\n";
                    log.setText(logMsg);
                    break;
                case UPDATALOG:
                    log.setText(logMsg);
                    //set Scroll
                    log.setScrollContainer(true);
                    break;
            }
        }
    };
}
