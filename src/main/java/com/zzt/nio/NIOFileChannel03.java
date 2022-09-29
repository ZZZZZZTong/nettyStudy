package com.zzt.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {

        //input 把文档里数据放入通道里
        FileInputStream fileInputStream = new FileInputStream("/Users/bytedance/zzt.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("/Users/bytedance/zzt2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();
        //给buffer分配空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true){
            //清空buffer
            /*
            * 重置buffer的属性
            * public final Buffer clear(){
            *       position =0;
            *       limit = capacity;
            *       mark=-1;
            *       return this;
            * }
            * */
            byteBuffer.clear();
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read =" +read);
            if (read == -1){
                break;
            }
            //将buffer中的数据写入到fileChannel02
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }
        //关闭流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
