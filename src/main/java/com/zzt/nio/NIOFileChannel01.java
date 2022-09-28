package com.zzt.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {

        //汉字占3个字节
        String str = "hello,尚硅谷";

        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/bytedance/zzt.txt");

        //通过 fileOutputStream 获取对应的FileChannel
        //这个 fileChannel 真实是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入byteBuffer
        byteBuffer.put(str.getBytes());

        //对byteBuffer 进行flip
        byteBuffer.flip();

        //将byteBuffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
