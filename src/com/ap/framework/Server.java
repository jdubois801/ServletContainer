package com.ap.framework;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import javax.servlet.ServletException;

public class Server {
    ServletContextImpl ctx = new ServletContextImpl();

    private void run() throws IOException, ServletException {
        ServerSocket socket = new ServerSocket(8083);

        System.out.println("Servlet listening on port 8083");
        while (true) {
           Socket sock = socket.accept();
            new Dispatcher(ctx, sock).service();

            sock.close();
        }
    }

    public static void main(String[] args) {
        try {
            new Server().run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
