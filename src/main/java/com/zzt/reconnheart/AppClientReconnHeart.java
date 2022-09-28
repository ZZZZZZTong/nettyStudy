package com.zzt.reconnheart;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.lang.management.ThreadInfo;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


//客户端启动类
public class AppClientReconnHeart {
    private final String host ;
    private final int port;

    public AppClientReconnHeart(String host, int port){
        this.host=host;
        this.port=port;
    }

    public void run() throws Exception{

        //配置相应参数，提供连接到远端的方法

        //就当做是 io的线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap(); //客户端辅助启动类
            bs.group(group)
                    .channel(NioSocketChannel.class) //实例化一个通道
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() { //进行通道初始化配置

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加自定义的Handler
                            //pipeline 责任链 或者看做是handler的容器
                            //超时检测2 添加IdleStateHandler，如果在5秒内没有发送消息 则读超时，就触发userEventTriggered
                            socketChannel.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new HandlerClientReconnHeart());
                        }
                    });
            // 通过ChannelFuture 可以获得系统里各种各样的事件
            ChannelFuture f = bs.connect(); //同步改为异步
            //添加监听
            f.addListener(new ListenerClientReconnHeart());

            while (true){
                Thread.sleep(6000);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = df.format(new Date());
                f.channel().writeAndFlush(Unpooled.copiedBuffer(strDate, CharsetUtil.UTF_8));
            }

            //发送消息到服务器端，编码格式为utf-8 write是写flush是刷新和提交
            //通过Unpooled.copiedBuffer将string 转换为byteBuf
//            f.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));

//            f.channel().closeFuture().sync();

        } finally {
            //释放资源  把管理的对象线程池都释放才退出
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {
        new AppClientReconnHeart("127.0.0.1",18080).run();
    }
}
