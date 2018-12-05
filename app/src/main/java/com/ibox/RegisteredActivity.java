package com.ibox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class RegisteredActivity extends AppCompatActivity {

    EditText  username;
    EditText password;
    EditText phonenumber;
    EditText  address;
    Button registered;
    Button  backlogin;
    String user;
    String pass;
    String phone;
    String addresses;
    String request;
    private  final String IP="192.168.43.125";
    private final int PORT=8888;
    private static Socket socket;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==1)
           {
               SharedPreferences.Editor editor = getSharedPreferences("Users", MODE_PRIVATE).edit();
               editor.putString("username", user);
               editor.putString("password", pass);
               editor.putString("phonenumber", phone);
               editor.apply();
               Toast.makeText(RegisteredActivity.this, "注册成功，去登录吧", Toast.LENGTH_SHORT).show();
           }
           if(msg.what==-1)
           {
               username.setText("");
               password.setText("");
               phonenumber.setText("");
               address.setText("");
               Toast.makeText(RegisteredActivity.this, "用户名或者手机号已经存在，请重新输入", Toast.LENGTH_SHORT).show();
           }
           if(msg.what==0)
           {
               Toast.makeText(RegisteredActivity.this,"注册失败，未知原因错误",Toast.LENGTH_SHORT).show();
           }
        }
    } ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        getSupportActionBar().hide();
        username=(EditText)findViewById(R.id.Registered_username);
        password=(EditText)findViewById(R.id.Registered_password);
        phonenumber=(EditText)findViewById(R.id.Registered_phonenumber);
        registered=(Button)findViewById(R.id.Registered_registered);
        backlogin=(Button)findViewById(R.id.Registered_backlogin);
        address=(EditText)findViewById(R.id.Registered_address);

        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= String.valueOf(username.getText());
                pass=String.valueOf(password.getText());
                phone=String.valueOf(phonenumber.getText());
                addresses=String.valueOf(address.getText());
                if(user.equals("")||pass.equals("")||phone.equals("")||address.equals(""))
                {
                    Toast.makeText(RegisteredActivity.this,"请将用户名、密码、手机号补充完整",Toast.LENGTH_SHORT).show();

                }else {
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("state", "user_regist");
                        jsonObject.put("username", user);
                        jsonObject.put("password", pass);
                        jsonObject.put("telenumber", phone);
                        jsonObject.put("address", addresses);
                        jsonObject.put("IP", getIP(RegisteredActivity.this));
                        jsonObject.put("port", "8887");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnection connection = null;
                          //  DataInputStream in = null;
                            //DataOutputStream out = null;
                            URL url = null;
                            int flag = 0;
                            try {
                                /*url = new URL("http://192.168.43.125:8888");
                                connection = (HttpURLConnection) url.openConnection();
                                out = new DataOutputStream(connection.getOutputStream());
                                in = new DataInputStream(connection.getInputStream());*/
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
                                e.printStackTrace();
                            }
                        }
                    }).start();



                }
            }

        });
        backlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisteredActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public  String getIP(Context context){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
