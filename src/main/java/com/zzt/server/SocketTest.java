package com.zzt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {
    public static void main(String[] args)  {
        try (ServerSocket server= new ServerSocket(8080)){
            System.out.println("等待客户端连接。。。。。");
                Socket s = server.accept();
                System.out.println("客户端已连接，IP地址为："+s.getInetAddress().getHostAddress());
                BufferedReader reader= new BufferedReader(new InputStreamReader(s.getInputStream()));
                System.out.println("接收到客户端数据：");
                System.out.println(reader.readLine());
                while (reader.ready()) System.out.println(reader.readLine());
                OutputStreamWriter writer=new OutputStreamWriter(s.getOutputStream());
                writer.write("HTTP/1.1 200 Accepted\r\n");//200是响应码，Http协议规定200为接受请求，400为错误的请求，404为找不到此资源（不止这些，还有很多）
                writer.write("\r\n");  //在请求头写完之后还要进行一次换行。然后写入我们的响应实体（会在浏览器上展示的内容）
                writer.write("lbwnb!");
                writer.flush();
                s.close(); //和服务端TCP连接完成后，记得关闭

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
