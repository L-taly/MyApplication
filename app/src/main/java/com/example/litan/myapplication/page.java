package com.example.litan.myapplication;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.IpPrefix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.InputStreamReader;
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
        setonclick();
        init();

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

    public void init(){
        log.setMovementMethod(ScrollingMovementMethod.getInstance());
        logMsg = log.getText().toString();
        socket = new Socket();
        ip = onLoad();
        if(ip != null){
            ipEdit.setText(ip);
        }
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
    public String RevciveMsg(Socket socket)throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        String txt = "";
        while ((line = reader.readLine()) != null){
            txt += line + "\n";
        }
        reader.close();
        return txt;
    }

    public class tcpClient extends Thread {
        String commandstring;

        public tcpClient(){
            commandstring = "1s";
        }

        public tcpClient(String command){
            commandstring = command;
        }

        public void run(){
            String recv;
            try{
                ConnectToServer();
                //send command to server
                SendMsg(socket, commandstring);
                //recieve massage from server
                recv = RevciveMsg(socket);
                if(recv != null){
                    logMsg += recv;
                    writer.close();
                    socket.close();
                    Message msg = new Message();
                    msg.what = UPDATALOG;
                    mHandler.sendMessage(msg);
                }
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private String onLoad(){
        SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
        //get IP from mPreferences
        String mPreferences = setting.getString("preferences", DEFAULT_IP);
        return mPreferences;
    }

    private void onSave(String save){
        if(TextUtils.isEmpty(save)){
            //Length = 0
            // null
            setPreferences(DEFAULT_IP);
        }
        else {
            setPreferences(save);
        }
    }

    private void setPreferences(String mPreferences){
        //setting configuration parameters
        SharedPreferences setting = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("preferences", mPreferences);
        editor.commit();
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("PC Control");
            builder.setMessage("exit ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onSave(ipEdit.getText().toString());
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
        return super.onKeyDown(KeyCode, event);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, 1, 1, "poweroff");
        menu.add(0, 2, 2, "reboot");
        menu.add(0, 3, 3, "exit");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //
            case 1:
                tcpClient tcp = new tcpClient("sudo poweroff");
                tcp.start();
                return true;
            case 2:
                tcp = new tcpClient("sudo reboot");
                tcp.start();
                return true;
            case 3:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //Judge connection state
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
