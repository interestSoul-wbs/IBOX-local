package com.ibox;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ShowActivity extends AppCompatActivity {

    private ExpressShow[]  expresses;
    private List<ExpressShow> expressList=new ArrayList<ExpressShow>();
    private  ExpressAdapter  expressAdapter;
    private Toolbar  toolbar;
    private DrawerLayout drawerLayout;
    private Button  button;
    private TextView  d_userID;
    private TextView  d_useranme;
    private  Socket socket;
    private final String IP="192.168.43.125";
    private  final int PORT=8888;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                JSONObject jsonObject=new JSONObject(msg.obj.toString());
               // Toast.makeText(ShowActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                int count=jsonObject.getInt("count");
                if(count==0)
                {
                    expressList.clear();
                    Toast.makeText(ShowActivity.this,"您当前没有订单",Toast.LENGTH_SHORT).show();

                }else {
                    expressList.clear();

                    for (int i = 0; i < count; i++) {
                        String ordernumber = jsonObject.getString(Integer.toString(i));

                        expressList.add(new ExpressShow("订单编号:"+ordernumber, R.drawable.track2));
                    }

                }

                expressAdapter=new ExpressAdapter(expressList);
                recyclerView.setAdapter(expressAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    public  void initExpresses()

    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject=new JSONObject();
                try {
                    SharedPreferences users = getSharedPreferences("Users", MODE_PRIVATE);
                    jsonObject.put("state","order_show");
                    jsonObject.put("userID",users.getString("userID","22"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    socket=new Socket();
                    socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(jsonObject.toString());
                    String s=in.readUTF();
                    Message  message=new Message();
                    //message.
                    message.obj=s;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
         // expressList.clear();
        //for(int i=0;i<5;i++)
        //{
           // expressList.add(expresses[0]);
           // expressList.add(expresses[1]);
        //}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
       toolbar=(Toolbar)findViewById(R.id.toolbar_show);
        fab=(FloatingActionButton)findViewById(R.id.scancode);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.view_nav);
        ActionBar actionBar=getSupportActionBar();
         recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager  layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);

        //初始化个人信息
        Intent intent=getIntent();
        View headview=navigationView.getHeaderView(0);
        d_userID=(TextView) headview.findViewById(R.id.icon_id);
        d_userID.setText(intent.getStringExtra("userID"));
        d_useranme=(TextView)headview.findViewById(R.id.icon_name);
        d_useranme.setText(intent.getStringExtra("username"));
        /*Intent intent2=getIntent();
        int alter=intent2.getIntExtra("alter",0);
        if(alter==1)
        {
            Toast.makeText(ShowActivity.this, alter, Toast.LENGTH_SHORT).show();
        }*/

         initExpresses();

        //SharedPreferences orders= getSharedPreferences("Orders", MODE_PRIVATE);
       // int count=orders.getInt("count",0);
       // Toast.makeText(ShowActivity.this,Integer.toString(count),Toast.LENGTH_SHORT).show();
       // for(int i=0;i<count;i++)
       // {
            //Toast.makeText(ShowActivity.this,orders.getString(Integer.toString(i),""),Toast.LENGTH_SHORT).show();
        //    expressList.add(new ExpressShow(orders.getString(Integer.toString(i),""),R.drawable.login));
       // }

         //Toast.makeText(ShowActivity.this,expressList.get(0).getOrdernumber(),Toast.LENGTH_SHORT).show();
        //expressAdapter=new ExpressAdapter(expressList);
        //recyclerView.setAdapter(expressAdapter);

        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name2);
        }

        //扫描二维码模块
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(ShowActivity.this,ScanActivity.class);
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
            case R.id.addExpress:
                AlertDialog.Builder dialog=new AlertDialog.Builder(ShowActivity.this);
                dialog.setTitle("提示：");
                dialog.setMessage("确定要创建新的快递订单吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ShowActivity.this,ExpressOrderSubmitActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.update:
                //Intent intent=new Intent(ShowActivity.this,ShowActivity.class);
                //startActivity(intent);
                initExpresses();
                 break;
            default:break;
        }
        return  true;
    }

}
