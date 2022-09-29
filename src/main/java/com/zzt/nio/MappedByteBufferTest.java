package com.zzt.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception{
        //MappedByteBuffer 可以让文件在(堆外内存)内存中进行修改，操作系统不需要拷贝一次

        RandomAccessFile r =  new RandomAccessFile("/Users/bytedance/zzt.txt","rw");

        FileChannel channel=r.getChannel();

        // 读写模式，起始位置，映射到内存的大小 也就是文件中可映射到内存的字节数
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0,(byte)'H');
        map.put(3,(byte)'l');
        map.put(4,(byte)'0');

        r.close();


    }
}
