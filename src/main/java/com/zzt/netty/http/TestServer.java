package com.zzt.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {

    public static void main(String[] args) throws Exception{

        //1.创建两个线程组
        //2.boosGroup 只处理连接请求，真正的和客户端业务处理，会交给wokerGroup完成
        //3.两个都无线循环
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //创建服务器端的启动对象，配置参数
            //1.设置两个线程组，设置NIO作为服务器的通道实现
            serverBootstrap.group(boosGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInitializer());
            //谷歌浏览器禁止访问6668
            //
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();

            channelFuture.channel().closeFuture().sync();
        }finally {

            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
