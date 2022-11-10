package com.zzt.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class GroupChatServer {

    private int port ;//监听端口

    public GroupChatServer(int port){
        this.port=port;
    }
    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        ServerBootstrap b =new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                //日志handler
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //向pipeline加入解码器
                        pipeline.addLast("decoder",new StringDecoder());
                        //向pipeline加入编码器
                        pipeline.addLast("encoder",new StringEncoder());
                        //加入自己业务处理Handler
                        pipeline.addLast(new GroupChatServerHandler());

                        //添加心跳机制监测handler
                        pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                        //添加处理心跳机制的自定义handler
                        pipeline.addLast(new HeartHandler());
                    }
                });
        System.out.println("netty 服务器启动");
        ChannelFuture channelFuture = b.bind(port).sync();

        //监听关闭
        channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new GroupChatServer(7000).run();

    }
}
