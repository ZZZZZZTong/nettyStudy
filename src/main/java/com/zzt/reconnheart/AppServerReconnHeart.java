package com.zzt.reconnheart;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

//服务器端启动类
public class AppServerReconnHeart {

    private int port;

    public AppServerReconnHeart(int port){
        this.port=port;
    }

    public void run() throws Exception{

        //netty的reactor线程池，初始化了一个NioEventLoop数组，用来处理I/O操作，如接受新的连接和读/写数据
        EventLoopGroup group= new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();// 用于启动NIO服务
            b.group(group)
                    .channel(NioServerSocketChannel.class) //通过工厂方法实例化一个channel
                    .localAddress(new InetSocketAddress(port))//设置监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        //ChannelInitializer 是一个特殊的处理类，他的目的配置一个新的channel
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //配置childHandler来通知一个关于消息处理的InfoServerHandler实例
                            ch.pipeline().addLast(new HandlerServerReconnHeart());
                        }
                    });

            //绑定服务器，该实例将提供有关IO操作的结果或状态的信息
            ChannelFuture channelFuture= b.bind().sync();
            System.out.println("在"+channelFuture.channel().localAddress()+"上开启监听");

            //只有通道关闭才会走到这
            //阻塞操作，closeFuture()开启了一个channel的监听器(这期间channel在进行各项工作)，直到链路断开
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup并释放所有资源,包括所有创建的线程
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {
        new AppServerReconnHeart(18080).run();
    }

}
