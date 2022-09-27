package com.zzt.monitorevent;


import com.zzt.handler.HandlerClientHello;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


//客户端启动类
public class AppClientMonitorEvent {
    private final String host ;
    private final int port;

    public AppClientMonitorEvent(String host, int port){
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
                            socketChannel.pipeline().addLast(new HandlerClientMonitorEvent());
                        }
                    });
            // 通过ChannelFuture 可以获得系统里各种各样的事件
            ChannelFuture f = bs.connect().sync(); //同步

            //发送消息到服务器端，编码格式为utf-8 write是写flush是刷新和提交
            //通过Unpooled.copiedBuffer将string 转换为byteBuf
//            f.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));

            f.channel().closeFuture().sync();

        } finally {
            //释放资源  把管理的对象线程池都释放才退出
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {
        new AppClientMonitorEvent("127.0.0.1",18080).run();
    }
}
