package com.zzt.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScaAndGath {

    //buffer数组进行写入 读取
    //scattering:将数据写入到buffer时，可以采用buffer数组，一次写入
    //Gathering : 从buffer读取数据时，采用buffer数组，依次读取
    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket 并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8; //假定从客户端接收8个字节

        while (true){

            int byteRead =0;
            while (byteRead < messageLength) {
                long r = socketChannel.read(byteBuffers);

                byteRead += r;
                System.out.println("byteRead = " + byteRead);
                //使用流打印，看看当前这个buffer的position 和limit
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position=" + byteBuffer.position() + ", limit = " + byteBuffer.limit()).forEach(System.out::println);
                // 将所有的buffer进行flip
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());
            }
                //将数据读出显示到客户端
                long byteWrite= 0;
                while (byteWrite < messageLength){
                    long l = socketChannel.write(byteBuffers);
                    byteWrite+=l;
                }

                //将所有的buffer进行clear

                Arrays.asList(byteBuffers).forEach(byteBuffer -> {
                    byteBuffer.clear();
                });
                System.out.println("byteRead := " + byteRead + "byteWrite=" + byteWrite +", messagelength"+ messageLength);


        }

    }
}
