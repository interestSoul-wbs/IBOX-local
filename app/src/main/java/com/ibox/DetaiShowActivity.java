package com.ibox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetaiShowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private  Button  button;
    private Button    graphic;
    private Button    map;
    private  TextView textView;
    private DrawerLayout  drawerLayout;
    private TextView  d_userID;
    private TextView  d_useranme;
    private TextView ordernumber;
    private TextView sendID;
    private TextView sendAdd;
    private TextView  receiveID;
    private TextView receiveAdd;
    private TextView  temperature;
    private TextView  humidity;
    private TextView  location;
    private TextView  ifpengzhuang;
    private TextView    issafe;
    private String info;
    private String ordertime;
    private long  starttime;
    private  TextView   tv_timer;
    private long currentSecond=0 ;
    private String  timeStr="";
    private String IP="192.168.43.125";
    private int PORT=8888;
    public    Handler mhandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           // super.handleMessage(msg);
            if(msg.what==1)
            {
                //textView.setText((String)msg.obj);
                //Toast.makeText(DetaiShowActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                String s=(String)msg.obj;
                if(s.equals("-1"))
                {
                    Toast.makeText(DetaiShowActivity.this, "抱歉，当前暂无信息", Toast.LENGTH_SHORT).show();

                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        ordernumber.setText(jsonObject.getString("ordernumber"));
                        sendID.setText(jsonObject.getString("sendID"));
                        sendAdd.setText(jsonObject.getString("sendaddress"));
                        receiveID.setText(jsonObject.getString("receiveID"));
                        receiveAdd.setText(jsonObject.getString("receiveaddress"));
                        temperature.setText(jsonObject.getString("temperature"));
                        humidity.setText(jsonObject.getString("humidity"));
                        location.setText(jsonObject.getString("location"));
                        if(jsonObject.getString("ifpengzhuang").equals("YES")) {
                            ifpengzhuang.setText(jsonObject.getString("ifpengzhuang"));
                            ifpengzhuang.setTextColor(Color.RED);
                        }else
                        {
                            ifpengzhuang.setText(jsonObject.getString("ifpengzhuang"));
                            ifpengzhuang.setTextColor(Color.GREEN);
                        }
                        issafe.setText(jsonObject.getString("issafe"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }



        }
    };
    private void startSocketService()
    {
        Intent intent=new Intent(this,SocketService.class);
        startService(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detai_show);
       // toolbar=(Toolbar)findViewById(R.id.toolbar_showdetail);
        button=(Button)findViewById(R.id.lookfor);
        NavigationView navigationView=(NavigationView)findViewById(R.id.view_nav_showdetail);
        View headview=navigationView.getHeaderView(0);
        SharedPreferences user = getSharedPreferences("User", MODE_PRIVATE);
        d_userID=(TextView) headview.findViewById(R.id.icon_id);
        d_userID.setText(user.getString("userID",""));
        d_useranme=(TextView)headview.findViewById(R.id.icon_name);
        d_useranme.setText(user.getString("username",""));
        ordernumber=(TextView)findViewById(R.id.express_ordernumber);
        sendID=(TextView)findViewById(R.id.sendID);
        sendAdd=(TextView)findViewById(R.id.sendaddress);
        receiveID=(TextView)findViewById(R.id.receiveID);
        receiveAdd=(TextView)findViewById(R.id.receiveaddress);
        temperature=(TextView)findViewById(R.id.temperature);
        humidity=(TextView)findViewById(R.id.humidity);
        location=(TextView)findViewById(R.id.location);
        ifpengzhuang=(TextView)findViewById(R.id.ifpengzhuang);
        issafe=(TextView)findViewById(R.id.issafe);
        graphic=(Button)findViewById(R.id.graphic_show);
        map=(Button)findViewById(R.id.map_show);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        //textView=(TextView)findViewById(R.id.message);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout_showdetail);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name);
        }

        info=getIntent().getStringExtra("ordernumber");
        ordernumber.setText(info);
        //计时功能
         ordertime=info.substring(2, 16);
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
        try {
             starttime=sf.parse(ordertime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        countTime();
        //
        //timeStr=info.substring()
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       JSONObject jsonObject=new JSONObject();
                       try {
                           jsonObject.put("state","express_info_request");
                           //jsonObject.put("")
                           //SharedPreferences users = getSharedPreferences("User", MODE_PRIVATE);
                          // String   username=users.getString("username","");
                          // int userID=Integer.parseInt(users.getString("userID",""));
                          // jsonObject.put("username",username);
                           //jsonObject.put("userID",userID);
                           jsonObject.put("ordernumber",info);

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       Socket socket=new Socket();
                       try {
                           socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                           DataInputStream in = new DataInputStream(socket.getInputStream());
                           DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                           out.writeUTF(jsonObject.toString());
                           //out.flush();
                            String s= in.readUTF();
                           in.close();
                           out.close();
                           //System.out.println();
                           // System.out.println(flag);
                           Message message=new Message();
                           message.what=1;
                           message.obj=s;
                           mhandler.sendMessage(message);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }

                   }
               }).start();
            }
        });
      graphic.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent  intent=new Intent(DetaiShowActivity.this,TemperatureShowActivity.class);
              startActivity(intent);
          }
      });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(DetaiShowActivity.this,MapshowActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case  R.id.addExpress:
                   break;
            default:break;
        }
        return  true;
    }
    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {

            currentSecond = new Date().getTime()-starttime;
            timeStr = TimeUtil.getFormatHMS(currentSecond);
            tv_timer.setText(timeStr);
            //tv_timer.postDelayed(this,1000);
            // handler.postDelayed(this,1000);
            //是关键
            countTime();
        }
    };
    public   void countTime()
    {
        mhandler.postDelayed(TimerRunnable,1000);
    }

}
