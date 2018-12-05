package com.ibox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ScanActivity extends AppCompatActivity {
    private  final String IP="192.168.43.125";
    private final int PORT=8888;
    private static Socket socket;
    private TextView content;
    private TextView ordernumber;
    private TextView safelevel;
    private Button  receive;
    private String result="2";
    private int w_T=0;
    private int w_H=0;
    private int w_A=0;
    private CompletedView T_progressbar;
    private CompletedView H_progressbar;
    private CompletedView  A_progressbar;

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject((String) msg.obj);
                        int receiveID = jsonObject.getInt("receiveID");
                        SharedPreferences users = getSharedPreferences("User", MODE_PRIVATE);
                        int userID= Integer.parseInt(users.getString("userID", "0"));
                        if (receiveID==userID) {
                            String order = jsonObject.getString("ordernumber");
                            ordernumber.setText(order);
                            int warn = jsonObject.getInt("level");
                            if(warn==0)
                            {
                                safelevel.setText("状态良好");
                                safelevel.setTextColor(Color.GREEN);
                            }else{
                                safelevel.setText("出现异常状况，建议和发件人协商");
                                safelevel.setTextColor(Color.RED);
                            }
                            w_A = jsonObject.getInt("A_times");
                            w_H = (int) jsonObject.getDouble("H_dur");
                            w_T = (int) jsonObject.getDouble("T_dur");
                            int cA = 0, cH = 0, cT = 0;
                            //设置圆形进度条的进度
                            while (cA < w_A * 10 || cH < w_H * 10 || cT < w_T * 10) {
                                if (cA < w_A * 10) {
                                    cA += 30;
                                    A_progressbar.setProgress(cA);
                                    try {
                                        Thread.sleep(90);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    A_progressbar.setProgress(cA);
                                }

                                if (cH < w_H * 10) {
                                    cH += 10;
                                    H_progressbar.setProgress(cH);
                                    try {
                                        Thread.sleep(90);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    H_progressbar.setProgress(cH);
                                }

                                if (cT < w_T * 10) {
                                    cT += 10;
                                    T_progressbar.setProgress(cT);
                                    try {
                                        Thread.sleep(90);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    T_progressbar.setProgress(cT);
                                }
                            }
                        }else
                        {
                            finish();
                            Intent intent=new Intent(ScanActivity.this,ShowActivity.class);
                            intent.putExtra("alter",1);
                            startActivity(intent);
                        }

                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    break;
                case 2:
                       Toast.makeText(ScanActivity.this,"签收成功！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        content=(TextView)findViewById(R.id.codecontent);
        startActivityForResult(new Intent(ScanActivity.this, CaptureActivity.class), 0);
        //
        ordernumber=(TextView)findViewById(R.id.receive_orderbumber);
        receive=(Button)findViewById(R.id.received);
        safelevel=(TextView)findViewById(R.id.safelevelshow);
        //当点击receive按钮的时候向服务器发送信息
        receive.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           final JSONObject jsonObject = new JSONObject();
                                           try {
                                               jsonObject.put("state", "good_receive_tobox");

                                               jsonObject.put("boxID",result);
                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                           }
                                           new Thread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   socket = new Socket();
                                                   try {
                                                       socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                                                       DataInputStream in = new DataInputStream(socket.getInputStream());
                                                       DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                                       out.writeUTF(jsonObject.toString());
                                                       String result = in.readUTF();
                                                       Message message = new Message();
                                                       message.obj = result;
                                                       message.what = 2;
                                                       handler.sendMessage(message);
                                                   } catch (IOException e) {
                                                       e.printStackTrace();
                                                   }

                                               }
                                           }).start();
                                       }
                                   });
        //三个进度条表示快递的状态
        T_progressbar=(CompletedView)findViewById(R.id.t_progressbar);
        H_progressbar=(CompletedView)findViewById(R.id.h_progressbar);
        A_progressbar=(CompletedView)findViewById(R.id.a_progressbar);

    }
    /*class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
                try {
                    Thread.sleep(90);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //mImage.setVisibility(View.VISIBLE);
            // mContent.setVisibility(View.GONE);
            //  Bundle bundle = data.getExtras();
            // String result = bundle.getString("result");
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            //Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);
            // Bundle bundle = data.getExtras();
            // String result = bundle.getString("result");
            //tv_result.setText(result);

            //  mResult.setText("扫描结果为：" + result);
            //showToast("扫码结果："+result);
            // if (bitmap != null) {
            //    mImage.setImageBitmap(bitmap);
            // }

        }else if (resultCode == RESULT_CANCELED) {
            result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
           // showToast("扫描结果"+result);
           // content.setText(result);
            final JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("state","good_receive_info");
                jsonObject.put("boxID",result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    socket=new Socket();
                    try {
                        socket.connect(new InetSocketAddress(IP, PORT), 5 * 1000);
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        out.writeUTF(jsonObject.toString());
                        String result=in.readUTF();
                        Message message=new Message();
                        message.obj=result;
                        message.what=1;
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }


    }

    private void showToast(String msg) {
        Toast.makeText(ScanActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
    }

}
