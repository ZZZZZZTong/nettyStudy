package com.zzt.reconnheart;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//通过接口ChannelFutureListener 来实现客户端的自动重连，有别于断线重连
public class ListenerClientReconnHeart implements ChannelFutureListener {

    private AppClientReconnHeart appClientReconnHeart= new AppClientReconnHeart("127.0.0.1",18080);
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {

        if (!channelFuture.isSuccess()){

            EventLoop loop  = channelFuture.channel().eventLoop();
            ScheduledFuture<?> schedule = loop.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("自动启动客户端，开始连接服务器");
                        appClientReconnHeart.run();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            },5, TimeUnit.SECONDS);
        }else {
            System.out.println("服务器连接成功。。。");
        }
    }
}
