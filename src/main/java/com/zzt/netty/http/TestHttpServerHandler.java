package com.zzt.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;

//1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter的子类
//2. HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //判断 msg 是否为httprequest请求


    }
}
