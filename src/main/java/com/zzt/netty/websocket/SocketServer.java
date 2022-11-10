package com.zzt.netty.websocket;

import com.zzt.netty.groupchat.GroupChatServer;
import com.zzt.netty.groupchat.GroupChatServerHandler;
import com.zzt.netty.groupchat.HeartHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class SocketServer {
    private int port ;//监听端口

    public SocketServer(int port){
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

                            //因为基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写，添加ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            *
                            1.http数据在传输过程中是分段，HttpObjectAggregator ，就是可以将多个段聚合
                            2.这就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                            * */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            //对应websocket，它是以帧(frame)形式传递
                            //可以看到webSocketFrame 下面有六个子类
                            //浏览器请求时：ws://localhost:7000/hello 表示请求的uri
                            //WebSocketServerProtocolHandler 核心功能将http协议升级为ws协议，保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义handler
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
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

        new SocketServer(7000).run();

    }
}
