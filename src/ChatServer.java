import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Program: OnlineChatSystem
 * @Author: XuSong
 * @Description: 服务器端
 * @Data: Created on 2018-11-20 22:27
 */
public class ChatServer {
    //表示server有没有起来，有没有监听好，初始值为false
    boolean started = false;
    //服务器端监听的socket
    ServerSocket ss = null;

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        try {
            //创建绑定到指定端口(8888)的服务器套接字，表示监听在8888端口
            ss = new ServerSocket(8888);
            //将状态设为true，表示已经启动server
            started = true;
            //处理端口已被占用的异常
        } catch (BindException e) {
            System.out.println("端口已被占用。。。");
            System.out.println("请关闭相关程序，并重新运行服务器");
            System.exit(0);
            //解决读写数据的异常
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //需要接受很多个客户端
            while (started) {
                //只要已经启动，就不断去接收客户端的连接
                Socket s = ss.accept();
                //接收进来以后创建一个单独的线程
                Client c = new Client(s);
                //连接成功后输出信息
                System.out.println("客户端已连接");
                //启动线程
                new Thread(c).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Client implements Runnable {
    //包装了和每一个客户端的单独的连接
    private Socket s;
    //从socket里面读内容的输入管道
    private DataInputStream dis = null;
    //代表是否已经连接上
    private boolean bConnected = false;


    public Client(Socket s) {
        this.s = s;
        try {
            dis = new DataInputStream(s.getInputStream());
            bConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (bConnected) {
                String str = dis.readUTF();
                System.out.println(str);
            }
        } catch (EOFException e) {
            System.out.println("客户端已关闭");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null)
                    dis.close();
                if (s != null)
                    s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
