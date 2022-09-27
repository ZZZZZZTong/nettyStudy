package com.zzt.monitorevent;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HandlerServerMonitorEvent extends ChannelInboundHandlerAdapter {

    //通道数组，保存所有注册到EventLoop的通道
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //处理收到的数据，并反馈到客户端
        //服务器端收到的消息为Object，因为连接的客户端多
        ByteBuf in = (ByteBuf) msg;
        System.out.println("收到客户端发过来的消息："+in.toString(CharsetUtil.UTF_8));
        //写入并发送信息到远端(客户端)

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss SSS");//设置时间格式
        String strDate = df.format(new Date());
        ctx.writeAndFlush(Unpooled.copiedBuffer("这是服务器端read方法中反馈的消息"+strDate+"\r\n",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //出现异常的时候执行的动作(打印并关闭通道)
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //新建立连接时触发的动作
        Channel incoming = ctx.channel();
        System.out.println("客户端"+incoming.remoteAddress()+"已经连接上来");
        channels.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        Channel incoming = ctx.channel();
        System.out.println("客户端"+incoming.remoteAddress()+"已断开");
        channels.remove(incoming);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //该方法只会在通道建立时调用一次，通道处于活动状态触发的动作
        Channel incoming = ctx.channel();
        System.out.println("客户端"+incoming.remoteAddress()+"在线");

        //写入并发送信息到远端(客户端)
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss SSS");
        String strDate = df.format(new Date());
        ctx.writeAndFlush(Unpooled.copiedBuffer("这是服务器端在Active方法中反馈的消息"+strDate,CharsetUtil.UTF_8));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("客户端"+incoming.remoteAddress()+"掉线");
    }
}
