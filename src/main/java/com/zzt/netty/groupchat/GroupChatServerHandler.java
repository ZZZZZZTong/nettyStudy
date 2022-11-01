package com.zzt.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channle组 ，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 连接建立，第一个被执行的函数
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    //将当前channel 加入channelGroup
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他客户端
        //该方法会将channelGroup中所有channel遍历一遍，并发送消息
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n");
        channelGroup.add(channel);
    }

    //断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了\n");
        System.out.println("当前channelGroup size "+ channelGroup.size());
    }

    //channel处于活动的状态，提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了");
    }

    //channel处于不活动状态，离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() +" 离线了");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    // 获取到当前channel
        Channel channel = ctx.channel();
        //遍历channelGroup 根据不同情况，回送不同消息

        channelGroup.forEach(ch -> {
            if (channel!=ch){
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了消息" +msg +"\n");
            }else {
                ch.writeAndFlush("[自己]发送了"+msg +"\n");
            }

        });
    }

    //处理异常

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
