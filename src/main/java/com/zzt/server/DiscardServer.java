package com.zzt.server;

import com.zzt.handler.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    private int port;

    public DiscardServer(int port){
        this.port=port;
    }
    public void run() throws Exception{
        EventLoopGroup boosGroup = new NioEventLoopGroup(); //1
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b =new ServerBootstrap(); //2
            b.group(boosGroup,workerGroup) //确定了线程模型
                    .channel(NioServerSocketChannel.class) //3  IO 模型为NIO
                    .childHandler(new ChannelInitializer<SocketChannel>() { //4
                        // 通过 .childHandler()给引导类创建一个ChannelInitializer ，
                        // 然后指定了服务端消息的业务处理逻辑也就是自定义的ChannelHandler 对象
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128) //5
                    .childOption(ChannelOption.SO_KEEPALIVE,true); //6
            // Bind and start to accept incoming connections.
            //bind()是异步的，但是，你可以通过 `sync()`方法将其变为同步
            ChannelFuture f = b.bind(port).sync(); //7

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length>0){
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}
