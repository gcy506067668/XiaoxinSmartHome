package letmesleep.online.controller;

import letmesleep.online.listener.SocketListener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Letmesleep on 2017/10/5.
 */
public class TemperatureHumidityServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String temperature = req.getParameter("temperature");
        String humidity = req.getParameter("humidity");
        SocketListener.queue.poll();
        SocketListener.queue.offer(temperature+"_"+humidity);
        System.out.println(temperature+"_"+humidity);
    }
}
