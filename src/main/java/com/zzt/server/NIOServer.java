package com.zzt.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception {

        //创建ServerSocketChannel -> 类比serverSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 serverSocketChannel 注册到selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            //这里等待1秒，如果没有事件发生，返回
            //select是阻塞
            if (selector.select(1000)==0){
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            //如果返回的>0，就获取到相关的selectionKey集合
            //1.如果返回的>0，表示已经获取到关注的事件
            //2.selector.selectedKeys()返回关注事件的集合
            //通过 selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 的数量："+selectionKeys.size());
            //遍历Set<SelectionKey> 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key 对应的通道发生的事件做响应的处理
                //如果是OP_ACCEPT 事件，有新的客户端连接
                //责任链
                if(key.isAcceptable()){
                    // 给该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("客户端连接成功，客户端是："+socketChannel.hashCode());
                    //将socketChannel 注册到selector,关注事件为OP_READ，给关联一个buffer

                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                if (key.isReadable()){//如果发生OP_READ
                    //通过key 反向获取channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int len =  channel.read(buffer);
                    //这里未对客户端是否关闭做监听 判断客户端是否关闭连接
                    if (len == -1){
                        channel.close();
                        keyIterator.remove();
                        continue;
                    }
                    System.out.println(new String(buffer.array()).length());
                    System.out.println("form 客户端" + new String(buffer.array()));
                }
                keyIterator.remove();

            }

        }

    }
}
