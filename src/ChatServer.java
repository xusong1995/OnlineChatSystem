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
    boolean started = false;
    ServerSocket ss = null;

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        try {
            //创建绑定到指定端口(8888)的服务器套接字。
            ss = new ServerSocket(8888);
            //将状态设为true
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
                Socket s = ss.accept();
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
    private Socket s;
    private DataInputStream dis = null;
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
