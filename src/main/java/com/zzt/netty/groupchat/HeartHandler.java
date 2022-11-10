package com.zzt.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartHandler extends ChannelInboundHandlerAdapter {


    //处理事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent){
            //将evt转换
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "--超时时间--" + eventType);
            System.out.println("服务器做响应处理");
        }

    }
}
