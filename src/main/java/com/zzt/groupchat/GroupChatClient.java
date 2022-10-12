package com.zzt.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    //定义相关属性
    private final String SERVER_HOST ="127.0.0.1";
    private final int SERVER_PORT =6667;

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    //构造器初始化工作
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = socketChannel.open(new InetSocketAddress(SERVER_HOST,SERVER_PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel 注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok...");


    }

    public void sendInfo(String msg){
        msg = username + " 说："+msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void readInfo(){

        try {
            int readChannels = selector.select(2000);
            if (readChannels >0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        //得到相关通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        //把读到的缓冲区的数据转成字符串
                        String s = new String(buffer.array());
                        System.out.println(s.trim());
                    }
                    iterator.remove();
                }

            }else {
//                System.out.println("没有可以用的通道。。。");
            }
        }catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception{

        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程 每3秒，读取从服务器发送的数据
        new Thread(){
            @Override
            public void run() {
                while (true){
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
