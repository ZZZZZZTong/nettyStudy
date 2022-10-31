package com.zzt.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {

    public static void main(String[] args) throws Exception{

        // 创建两个线程组 BossGroup 和WorkerGroup
        // BossGroup 只是处理连接请求，真正的和客户端业务处理交给workerGroup
        // 两个都是无限循环
        // bossGroup和workerGroup 含有的子线程(NioEventLoop)的个数 默认实际 cpu核数 * 2
        // 分配时连接的客户端会轮流的分配到workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //使用链式编程来进行设置
        //设置服务器端的配置
        bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class) //设置为NioSocketChannel 作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列 连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //可以用一个集合管理socketChannel，再推送消息时，将业务加入到对应的各个channel 对应的NIOEventLoop
                        //taskQueue 或者scheduleTaskQueue
                        System.out.println("客户端socketChannel hashcode = "+channel.hashCode());
                        channel.pipeline().addLast(new NettyServerHandler());  //
                    }
                }); //给我们wokerGroup 的EventLoop 对应的管道设置处理器

            System.out.println("服务器已启动");
        //绑定一个端口并且同步，生成了一个ChannelFuture对象
        //绑定端口后等于启动服务器
        ChannelFuture cf = bootstrap.bind(6668).sync();


        //给cf 注册监听器，监控我们关系的事件
            //对future对象做监听添加方法
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (cf.isSuccess()){
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });



        //对关闭通道这个消息进行监听
        cf.channel().closeFuture().sync();
    }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
