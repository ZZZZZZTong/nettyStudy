package com.zzt.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception{
        //客户端只需要一个事件循环组

        EventLoopGroup group = new NioEventLoopGroup();

        //创建客户端对象
        Bootstrap bootstrap = new Bootstrap();

        try {


            //设置
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });

            // 启动客户端去连接服务器端
            // 关于ChannelFuture 要分析，涉及netty的异步模型
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668).sync();

            cf.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
