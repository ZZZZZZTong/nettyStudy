package com.zzt.client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        //获得一个socketChannel
        SocketChannel socketChannel =  SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作..");
            }
        }
        //连接成功
        String str = "hello~";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        System.out.println(new String(buffer.array()).length());
        // 将buffer写入channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
