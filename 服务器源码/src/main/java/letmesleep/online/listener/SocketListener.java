package letmesleep.online.listener;

import letmesleep.online.sockets.SocketThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Letmesleep on 2017/10/5.
 */
public class SocketListener implements ServletContextListener {
    public static Queue<String> commandQueue = new LinkedList<String>();
    public static Queue<String> queue = new LinkedList<String>();
    private boolean flag = true;
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    ServerSocket s = new ServerSocket(10101);
                    while(flag){
                            Socket socket = s.accept();
                            new SocketThread(socket);
                    }
                } catch (IOException e) {
                        e.printStackTrace();
                }

            }
        }).start();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        flag = false;
    }
}
