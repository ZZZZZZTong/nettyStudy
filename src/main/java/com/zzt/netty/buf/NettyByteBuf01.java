package com.zzt.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //说明
        //1.创建 对象，该对象包含一个数组arr ， 是一个byte[]
        //2.在Netty的buffer种不需要是用flip进行反转
        //底层维护了readerIndex 和writerIndex
        //3. 通过readerindex 和writerIndex 和capacity，将buffer分成三个区域
        //0-----readIndex已经读取的区域
        //readerIndex----writerIndex，可读区域
        //writerIndex ---- capacity 可写区域
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        //输出
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
            //此读法会让readIndex不断增加
            System.out.println(buffer.readByte());
        }
    }
}
