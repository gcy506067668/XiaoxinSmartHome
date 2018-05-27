package letmesleep.online.controller;

import letmesleep.online.listener.SocketListener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Letmesleep on 2017/10/5.
 */
public class LightServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String message = SocketListener.commandQueue.poll();
        SocketListener.commandQueue.offer(message);

        if(message!=null){
            resp.getWriter().print(message);
        }
    }
}
