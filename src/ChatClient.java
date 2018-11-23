import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Program: OnlineChatSystem
 * @Author: XuSong
 * @Description: 客户端
 * @Data: Created on 2018-11-20 16:31
 */
public class ChatClient {
    public static void main(String[] args) {
        ChatClientFrame chatClientFrame = new ChatClientFrame("在线聊天系统", 700, 300, 1200, 800);
        chatClientFrame.connect();
    }
}

class ChatClientFrame extends Frame {
    TextField textField = new TextField();
    TextArea textArea = new TextArea();
    Socket s = null;
    DataOutputStream dos = null;

    ChatClientFrame(String s, int x, int y, int width, int height) {
        //调用父类Frame的构造方法，为主面板命名
        super(s);

        //设置主面板的位置以及尺寸
        setBounds(x, y, width, height);

        //添加textArea至面板上方
        add(textArea, BorderLayout.NORTH);

        //添加textField至面板下方
        add(textField, BorderLayout.SOUTH);

        //为textField添加监听器
        textField.addActionListener(new TextFieldListener());

        //调整布局显示
        pack();

        //设置Frame为可见状态
        setVisible(true);

        //用点击右上角按钮可以关闭窗口，此处用内部类实现
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //关闭窗口时首先关闭disconnect方法，清理资源
                disconnect();
                System.exit(0);
            }
        });
    }

    class TextFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            //拿出输入框中的文字内容，并去掉两边空格
            String str = textField.getText().trim();

            //将拿出来的文字显示在textArea里面
            textArea.setText(str);

            //按回车键后清空输入框中文字内容
            textField.setText("");

            //按回车键后将把文字发送到服务器端
            try {
                dos.writeUTF(str);
                dos.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void connect() {
        try {
            //连接服务器
            s = new Socket("127.0.0.1", 8888);
            //初始化输出流
            dos = new DataOutputStream(s.getOutputStream());
            //提示连接成功
            System.out.println("连接服务器成功！");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            dos.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
