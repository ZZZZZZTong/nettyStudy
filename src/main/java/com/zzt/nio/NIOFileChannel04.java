package com.zzt.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception{
        //创建相关的流

        FileInputStream fileInputStream = new FileInputStream("/Users/bytedance/zzt.txt");
        FileChannel sourceCh = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("/Users/bytedance/zzt3.txt");
        FileChannel destCh = fileOutputStream.getChannel();


        //使用transferForm完成拷贝  括号里是拷贝目标，
        destCh.transferFrom(sourceCh,0,sourceCh.size());

        fileInputStream.close();
        fileOutputStream.close();
    }



}
