package com.ibox;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServersocketService extends Service {
    Context  context;
    public ServersocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        context=getApplicationContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket=new ServerSocket(8887);
                    while(true) {
                        Socket client = serverSocket.accept();
                        DataInputStream in = new DataInputStream(client.getInputStream());
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        String message = in.readUTF();
                        try {
                            JSONObject jsonObject=new JSONObject(message);
                            if(jsonObject.get("state").equals("warn_info"))
                            {
                                Intent  intent=new Intent(context, DestoryOrderActivity.class);
                                SharedPreferences.Editor editor = getSharedPreferences("Destory", MODE_PRIVATE).edit();
                                editor.putString("telenumber",jsonObject.getString("telenumber") );
                                editor.putString( "order",jsonObject.getString("order"));
                                editor.putString("mess",jsonObject.getString("mess"));
                                editor.apply();
                                //intent1.putStrExtra("ordernumber",jsonObject.get("ordernumber"));
                                //intent.putExtra("order",jsonObject.getString("order"));
                                //intent.putExtra("telenumber",jsonObject.getString("telenumber"));
                                //intent.putExtra("mess",jsonObject.getString("mess"));
                               // intent1.putExtra("order",jsonObject.getString("order"));
//                                intent1.putExtra("mess",jsonObject.getString("mess"));
                                PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
                                NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification notification=new NotificationCompat.Builder(context)
                                        .setContentText("您的快递出现紧急状况！请点击查看")
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.ic_stat_warn)
                                        .setContentIntent(pi)
                                        .setAutoCancel(true)
                                        .build();
                                manager.notify(2,notification);
                                out.writeUTF("1");

                            }

                            //如果是快递信息
                            if(jsonObject.get("state").equals("order_notice"))
                            {
                                //首先将快递信息进行存储
                                // Toast.makeText(context,"hello",Toast.LENGTH_SHORT);
                                //发出通知
                                Intent  intent1=new Intent(context, ShowActivity.class);
                                PendingIntent pi = PendingIntent.getActivity(context,0,intent1,0);
                                NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification notification=new NotificationCompat.Builder(context)
                                        .setContentText("您有一份快递定单，点击可查看")
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.ic_stat_goods)
                                        .setContentIntent(pi)
                                        .setAutoCancel(true)
                                        .build();
                                manager.notify(1,notification);
                               // out.writeUTF("-1");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //如果是警告信息

                       // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        //out.writeUTF("hello");
                        in.close();
                        out.close();
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
