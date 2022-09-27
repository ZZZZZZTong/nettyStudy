package com.zzt.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


//通用handler，处理I/O事件
//1
@ChannelHandler.Sharable
public class HandlerClientHello extends SimpleChannelInboundHandler<ByteBuf> { //2

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
}
