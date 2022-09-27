package com.zzt.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketCli {
    public static void main(String[] args) {
        try (Socket s = new Socket()){

            Scanner scanner= new Scanner(System.in);
            //设置超时时间，与手动连接
            s.setKeepAlive(true);
            s.connect(new InetSocketAddress("localhost",8080),1000);
            System.out.println("已连接到服务端！");
            OutputStream stream= s.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            System.out.println("请输入要发送给服务端的内容：");
            String text = scanner.nextLine();
            writer.write(text+'\n');
            writer.flush();
            System.out.println("数据已发送:"+text);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("收到服务器返回"+reader.readLine());
        }catch (IOException e){
            System.out.println("服务端连接失败！");
            e.printStackTrace();
        }finally {
            System.out.println("客户端断开连接！");
        }
    }
}
