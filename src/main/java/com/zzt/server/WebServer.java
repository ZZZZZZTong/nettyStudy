package com.zzt.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class WebServer {
    //进行实例化操作
    private DatagramSocket socket=null;
    //端口号
    public WebServer(int port) throws SocketException {
        socket=new DatagramSocket(port);
    }
    //开启服务器
    public void start() throws IOException {
        System.out.println("开启服务器");
        while (true){
            //1、读取请求并分析
            DatagramPacket requstPacket=new DatagramPacket(new byte[4096],4096);
            socket.receive(requstPacket);
            String reques=new String(requstPacket.getData(),0,
                    requstPacket.getLength()).trim();
            //2、请求数据相应
            String response=process(reques);
            //3、把响应返回给服务器
            DatagramPacket responsePacket=new DatagramPacket(response.getBytes()
                    ,response.getBytes().length,requstPacket.getSocketAddress());
            socket.send(responsePacket);

            //日志
            System.out.printf("[%s:%d] req: %s; resp:%s \n",requstPacket.getAddress().toString(),
                    requstPacket.getPort(),reques,response);
        }
    }

    private String process(String reques) {
        //我们写最简单的服务器回溯
        return reques;
    }

    public static void main(String[] args) throws IOException {
        WebServer test01=new WebServer(9090);
        test01.start();
    }

}
