package com.zzt.reconnheart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


//通用handler，处理I/O事件
//1
@ChannelHandler.Sharable
public class HandlerClientReconnHeart extends SimpleChannelInboundHandler<ByteBuf> { //2

    //3
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        // 处理接收到的消息

        System.out.println("接收到的消息"+byteBuf.toString(CharsetUtil.UTF_8));
    }

    //4
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印IO异常
        cause.printStackTrace();
        ctx.close();
    }

    //第一次与服务器连接成功
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss SSS");
        String strDate = df.format(new Date());
        ctx.writeAndFlush(Unpooled.copiedBuffer("这是客户端通过Active方法发送的消息"+strDate+"\r\n",CharsetUtil.UTF_8));

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端掉线");
    }
}
