package com.ibox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText  password;
    Button  registered;
    Button login;
    private  final String IP="192.168.43.125";
    private final int PORT=8888;
    private static Socket socket;
    private final int INTERNET_FALURE=0;
    private  final int LOGIN_SUCCESS=1;
    private final int LOGIN_FAILURE=-1;


    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what)
            {
                case INTERNET_FALURE:
                    Toast.makeText(MainActivity.this,"请检查网络连接",Toast.LENGTH_SHORT).show();
                    break;
                case  LOGIN_FAILURE:
                    Toast.makeText(MainActivity.this,"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                    intent.putExtra("username",String.valueOf(username.getText()));
                    intent.putExtra("userID",Integer.toString(msg.what));
                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                    editor.putString("username", String.valueOf(username.getText()));
                    editor.putString("userID", Integer.toString(msg.what));
                    editor.apply();
                   // editor.putString("phonenumber", phone);
                    //editor.apply();
                    startActivity(intent);
                    finish();
                    break;

            }
        }
    };
        public void startInternetService()
        {
            Intent intent=new Intent(MainActivity.this,SocketService.class);
            startService(intent);
        }
        public void startNotificationService()
        {
            Intent intent=new Intent(MainActivity.this,ServersocketService.class);
            startService(intent);
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//取消actionbar
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        //注册按钮
        registered=(Button)findViewById(R.id.Registered);
        username=(EditText)findViewById(R.id.LoginUserName);
        password=(EditText)findViewById(R.id.LoginUserPassword);
        login=(Button)findViewById(R.id.Login);
        //自动登录，按照上一次注册的进行自动登录
        //isLogInBefore();
        //启动网络服务
        //startInternetService();
         //启动通知服务
        startNotificationService();

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               final String  user=String.valueOf(username.getText());
                final String pass=String.valueOf(password.getText());
                if(user.equals("")||pass.equals(""))
                {
                    Toast.makeText(MainActivity.this,"请将用户名、密码、手机号补充完整",Toast.LENGTH_SHORT).show();
                }else {
                    /*SharedPreferences users = getSharedPreferences("Users", MODE_PRIVATE);
                    if (user.equals(users.getString("username", ""))&&pass.equals(users.getString("password","")))
                    {
                       // Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                        startActivity(intent);
                    }*/
                    //如果本地没有需要查询服务器
                    //else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("state","user_login");
                                    jsonObject.put("username",user);
                                    jsonObject.put("password",pass);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                int flag = 0;
                                try {

                                    socket=new Socket();
                                    socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                                    DataInputStream in = new DataInputStream(socket.getInputStream());
                                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                    out.writeUTF(jsonObject.toString());
                                    //out.flush();
                                    flag = Integer.parseInt(in.readUTF());
                                    in.close();
                                    out.close();
                                    //System.out.println();
                                    // System.out.println(flag);
                                    Message message=new Message();
                                    message.what=flag;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    Message message=new Message();
                                    message.what=flag;
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                  //  }
                }
            }
        });

        //注册界面
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisteredActivity.class);
                startActivity(intent);
            }
        });
        //如果是第一次登陆，就是没有注册
        /*1、检查用户名，密码
              （1）先和手机上的进行比对，如果没有登录信息，再和服务器数据库进行比对，
              同样没有，提示“请先进行注册”
          2、跳转到注册界面、注册完成后，将数据记录到服务器和本地数据库，然后跳转到登录界面
          3、在登录界面进行登录进入应用

         */



        //第二次登录，实现自动登录


    }
    public  void  isLogInBefore()
     {
         SharedPreferences users = getSharedPreferences("Users", MODE_PRIVATE);
         if(!users.getString("username","").equals("")&&!users.getString("password","").equals(""))
         {
                 Intent intent=new Intent(MainActivity.this,ShowActivity.class);
             startActivity(intent);
         }
    }
}
