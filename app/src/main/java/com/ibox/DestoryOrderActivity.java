package com.ibox;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DestoryOrderActivity extends AppCompatActivity {

    public Button destory;
    public Button check;
    public Button call;
    public TextView  warn;
    public String tele;
    public String ordernumber;
    public String message="1";
    private  final String IP="192.168.43.125";
    private final int PORT=8888;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
         if(msg.what==1)
         {
             Toast.makeText(DestoryOrderActivity.this,"您的订单已经删除！",Toast.LENGTH_SHORT).show();
         }
        }
    };
    //public
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destory_order);
        destory=(Button)findViewById(R.id.todestory);
        check=(Button)findViewById(R.id.toCheck);
        call=(Button)findViewById(R.id.toConnect);
        warn=(TextView)findViewById(R.id.notice_message);
        //Intent intent=getIntent();
        SharedPreferences dest = getSharedPreferences("Destory", MODE_PRIVATE);
        tele=dest.getString("telenumber","18852897686");
        ordernumber=dest.getString("order","xxxxxxxxx");
        Toast.makeText(DestoryOrderActivity.this,ordernumber,Toast.LENGTH_SHORT).show();
        message=dest.getString("mess","1");
        if(message.equals("1"))
        {
            warn.setText("温度/湿度超过您指定的范围！");
            warn.setGravity(View.TEXT_ALIGNMENT_CENTER);
            warn.setTextSize(25);
            warn.setTextColor(Color.RED);
        }
        if(message.equals("2"))
        {
            warn.setText("您的快递发生严重碰撞！");
            warn.setGravity(View.TEXT_ALIGNMENT_CENTER);
            warn.setTextSize(25);
            warn.setTextColor(Color.RED);
        }

        destory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* AlertDialog.Builder dialog=new AlertDialog.Builder(DestoryOrderActivity.this);
                dialog.setTitle("提示：");
                dialog.setMessage("确定要放弃创建新的快递订单吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("state", "destory_order");
                                    jsonObject.put("ordernumber", ordernumber);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Socket socket = new Socket();
                                try {
                                    socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                                    DataInputStream in = new DataInputStream(socket.getInputStream());
                                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                    out.writeUTF(jsonObject.toString());
                                    //out.flush();
                                    Message message = new Message();
                                    message.what = Integer.parseInt(in.readUTF());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        Intent intent=new Intent(DestoryOrderActivity.this,ShowActivity.class);
                        startActivity(intent);
                    /*}
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();*/

            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(DestoryOrderActivity.this,DetaiShowActivity.class);
                intent.putExtra("ordernumber",ordernumber);
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+tele));
                    startActivity(intent);
                }catch(SecurityException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}
