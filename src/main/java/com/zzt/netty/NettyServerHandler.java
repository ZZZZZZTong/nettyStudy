package com.zzt.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

//自定义一个Handler 需要继承 netty规定好的HandlerAdapter
// 这时这个自定义的才可以称为Handler
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据事件

    //ChannelHandlerContext 上下文对象，含有管道pipeline 通道channel，地址
    //Object msg 客户端发送的数据 默认object

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //如果存在一个非常耗时的业务-》异步执行-》提交该channel对应
        //解决方案1 用户程序自定义的普通任务
        //使用以下方法可以将任务提交到 NIOEventLoop中的TaskQueue中执行
        //等于异步，让程序快速走完channelRead后的方法与channelReadComplete
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        System.out.println("服务器端读取线程" + Thread.currentThread().getName());
        System.out.println("server ctx =" + ctx);
        //channel 与pipeline 互相都可以找到对方，channel也能找到自己属于哪个eventLoop
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); // 本质是个双向链表

        //将msg 转换为ByteBuf
        //ByteBuf 是Netty提供的，不是NIO的ByteBuffer提供的
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());

        //用户自定义定时任务-》该任务提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                Thread.sleep(5*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("执行schedule，hello,客户端",CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println("发送异常"+ex.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);

    }


    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //将数据写入缓冲，再刷新
        //发送数据需要编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~",CharsetUtil.UTF_8));
    }

    //处理异常 ，一般关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
