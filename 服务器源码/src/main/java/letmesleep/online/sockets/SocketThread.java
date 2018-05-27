package letmesleep.online.sockets;

import letmesleep.online.listener.SocketListener;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Letmesleep on 2017/10/5.
 */
public class SocketThread {
    private PrintStream printWriter;
    private BufferedReader buf;
    private Socket socket;
    private Runnable r = new Runnable() {
        public void run() {
            try {
                printWriter = new PrintStream(socket.getOutputStream());
                buf = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                String line="";
                while((line=buf.readLine())!=null){
                    System.out.println("client:"+line);
                    SocketListener.queue.poll();
                    SocketListener.queue.offer(line);
                    printWriter.println("reps:"+line);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    buf.close();
                    printWriter.close();
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
    public SocketThread(Socket socket){
        this.socket = socket;
        new Thread(r).start();
    }





}
