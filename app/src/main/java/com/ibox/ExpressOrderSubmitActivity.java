package com.ibox;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ExpressOrderSubmitActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button  button;
    private EditText sendID;
    private EditText  receiveID;
    private EditText  boxID;
    private EditText  thing;
    private  EditText minT;
    private  EditText  maxT;
    private  EditText  minH;
    private  EditText   maxH;
    private  EditText  acce;
    private  final String IP="192.168.43.125";
    private final int PORT=8888;
    private static Socket socket;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==2)
            {
                Toast.makeText(ExpressOrderSubmitActivity.this,"请将订单填写完整",Toast.LENGTH_SHORT).show();
            }
           if(msg.what==1)
           {
               Toast.makeText(ExpressOrderSubmitActivity.this,"快递订单创建成功",Toast.LENGTH_SHORT).show();
               sendID.setText("");
               receiveID.setText("");
               boxID.setText("");
               thing.setText("");
               minT.setText("");
               maxT.setText("");
               minH.setText("");
               maxH.setText("");
               acce.setText("");
           }

            else{
               Toast.makeText(ExpressOrderSubmitActivity.this,"订单创建失败，请检查您的信息是否正确",Toast.LENGTH_SHORT).show();
           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_order_submit);
        toolbar=(Toolbar)findViewById(R.id.toolbar_ordersubmit);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        button=(Button) findViewById(R.id.order_submit);
        sendID=(EditText)findViewById(R.id.order_sendID);
       receiveID=(EditText)findViewById(R.id.order_receiveID);
        boxID=(EditText)findViewById(R.id.order_boxID);
        thing=(EditText)findViewById(R.id.order_thing);
        minT=(EditText)findViewById(R.id.order_minT);
        maxT=(EditText)findViewById(R.id.order_maxT);
        minH=(EditText)findViewById(R.id.order_minH);
        maxH=(EditText)findViewById(R.id.order_maxH);
        acce=(EditText)findViewById(R.id.order_accelation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject=new JSONObject();
                        int flag=0;
                        if(sendID.getText().toString().equals("")||receiveID.getText().toString().equals("")||boxID.getText().toString().equals("")||thing.getText().toString().equals("")||
                                minT.getText().toString().equals("")||maxT.getText().toString().equals("")||minH.getText().toString().equals("")||maxH.getText().toString().equals("")||acce.getText().toString().equals(""))
                        {
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);

                        }
                        else {
                            try {


                                jsonObject.put("state", "express_order_submit");
                                jsonObject.put("sendID", Integer.parseInt(String.valueOf(sendID.getText())));
                                jsonObject.put("receiveID", Integer.parseInt(String.valueOf(receiveID.getText())));
                                jsonObject.put("boxID", Integer.parseInt(String.valueOf(boxID.getText())));
                                jsonObject.put("thing", String.valueOf(thing.getText()));
                                jsonObject.put("minT", Float.parseFloat(String.valueOf(minT.getText())));
                                jsonObject.put("maxT", Float.parseFloat(String.valueOf(maxT.getText())));
                                jsonObject.put("minH", Float.parseFloat(String.valueOf(minH.getText())));
                                jsonObject.put("maxH", Float.parseFloat(String.valueOf(maxH.getText())));
                                jsonObject.put("acce", Double.parseDouble(String.valueOf(acce.getText())));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            socket = new Socket();
                            try {
                                socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                                DataInputStream in = new DataInputStream(socket.getInputStream());
                                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                out.writeUTF(jsonObject.toString());
                                //out.flush();
                                flag = Integer.parseInt(in.readUTF());
                                in.close();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //System.out.println();
                            // System.out.println(flag);
                            Message message = new Message();
                            message.what = flag;
                            handler.sendMessage(message);
                        }
                    }
                }).start();

            }
        });
    }
    @Override
    public boolean  onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.ordersubmit_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.back:
               // drawerLayout.openDrawer(GravityCompat.START);
                AlertDialog.Builder dialog=new AlertDialog.Builder(ExpressOrderSubmitActivity.this);
                dialog.setTitle("提示：");
                dialog.setMessage("确定要放弃创建新的快递订单吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ExpressOrderSubmitActivity.this,ShowActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case  R.id.call:
                //调用打电话的功能
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:18852897158"));
                    startActivity(intent);
                }catch(SecurityException e)
                {
                    e.printStackTrace();
                }
                break;
            default:break;
        }
        return  true;
    }

}
