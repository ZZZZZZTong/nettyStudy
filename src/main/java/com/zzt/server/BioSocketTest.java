package com.zzt.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioSocketTest {

    public static void main(String[] args) throws IOException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true){
            //此处会有一个主线程
            //监听，等待客户端连接 accept 会阻塞 ，如果没有客户端连接则卡在这
            System.out.println("等待客户端连接");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //创建一个线程，与客户端通讯
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    public static void handler(Socket socket){

        try {
            System.out.println("线程信息 id =" + Thread.currentThread().getId() +" 名字="+Thread.currentThread().getName());
            //字节数组
            byte[] bytes= new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true){
                //连接成功后 又会阻塞在read，等待客户端发送数据
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println("线程信息 id =" + Thread.currentThread().getId() +" 名字="+Thread.currentThread().getName());
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
