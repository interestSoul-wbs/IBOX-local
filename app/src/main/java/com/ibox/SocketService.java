package com.ibox;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.bumptech.glide.request.ThumbnailRequestCoordinator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketService extends Service {
     private  final String IP="192.168.43.125";
    private final int PORT=8888;
    private static Socket socket;
    //用来被初始化，向DetailShowActivityhandler传递消息
    public  static  Handler  DetailShowActivityhandler=new Handler();
    private Messenger messenger;
    public  Service  me;
    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onInit();
        me=this;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        // messenger=(Messenger)intent.getExtras().get("messenger");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket=new Socket();
                    socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);

                   // socket.setSoTimeout(3 * 1000);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String ip= InetAddress.getLocalHost().getHostAddress();
                    out.writeUTF(ip+" 8887");
                    String s=in.readUTF();
                   // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    System.out.println();
                    System.out.print(s);
                    in.close();
                    out.close();
                    //socket.close();
                        //br.close();

                        Message message = new Message();
                        message.what = 1;
                       message.obj=s;
                    /*try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }*/
                    DetailShowActivityhandler.sendMessage(message);

                   // Toast.makeText(getApplicationContext(),(String)message.obj,Toast.LENGTH_SHORT).show();



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    //建立网络连接
    public void onInit()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket=new Socket();
                    socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
