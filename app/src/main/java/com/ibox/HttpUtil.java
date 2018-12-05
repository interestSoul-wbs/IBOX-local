package com.ibox;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by lenovo on 2018/4/15.
 */

class requestThread extends Thread{
    private String request;
    private int  end;

    public  int getEnd()
    {
        return end;
    }
    public requestThread(String request)
    {
        this.request=request;
    }
    @Override
    public void run() {
        HttpURLConnection connection=null;
        DataInputStream  in=null;
        DataOutputStream  out=null;
        URL  url=null;
        try{
            url=new URL("http://192.168.43.125:8888");
            connection= (HttpURLConnection) url.openConnection();
            out=new DataOutputStream(connection.getOutputStream());
            in=new DataInputStream(connection.getInputStream());
            out.writeUTF(request);
            out.flush();
            out.close();
            end=Integer.parseInt(in.readUTF());
            in.close();
            System.out.println();
            System.out.println(end);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
public class HttpUtil {

     //public static  int flag=0;
     public static  int registRequet(String request)
     {
           int flag=0;
          requestThread thread=new requestThread(request);
         flag=thread.getEnd();return flag;

}
}
