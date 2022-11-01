package com.zzt.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {


        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));

        if (byteBuf.hasArray()){
            byte[] content = byteBuf.array();

            //将content转成字符串
            System.out.println(new String(content,Charset.forName("utf-8")));

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            //第一个参数是输出第一个的索引下标，第二个参数是输出多少个
            System.out.println(byteBuf.getCharSequence(0,3,Charset.forName("utf-8")));
        }
    }
}
