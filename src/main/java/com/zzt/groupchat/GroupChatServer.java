package com.zzt.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    //定义属性
    private static Selector selector;
    private static ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer(){
        try{

            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    //监听
    public static void main(String[] args) throws Exception{
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();

        groupChatServer.listenServer();
    }

    private void listenServer(){
        try {
            while (true){
                //selector2秒获取一次
                int count = selector.select(2000);
                if (count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将客户端socket注册到selector
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }
                        if(key.isReadable()){ //当通道发生read，即通道为可读状态
                            //处理读
                            readData(key);
                        }
                        //将iterator移除
                        iterator.remove();
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    private void readData(SelectionKey key){
        SocketChannel channel= null;
        try {
            //得到channel
            channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer buffer  = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count的值做处理
            if (count>0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("from 客户端："+msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg,channel);
            }
        }catch (Exception e){
            try {
                System.out.println(channel.getRemoteAddress() +"离线了。。。");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }

        }

    }
    private void sendInfoToOtherClients(String msg,SocketChannel self ) throws Exception{
        System.out.println("服务器转发消息中");
        //遍历 所有注册到selector 上的SocketChannel，并排除self
        for(SelectionKey key:selector.keys()){
            //通过key 取出SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel !=self){
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer 的数据写入通道
                dest.write(buffer);
            }
        }



    }
}
