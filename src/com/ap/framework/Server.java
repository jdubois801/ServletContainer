package com.ap.framework;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import javax.servlet.ServletException;

public class Server {
    ServletContextImpl ctx = new ServletContextImpl();

    private void run() throws IOException, ServletException {
        ServerSocket socket = new ServerSocket(ctx.getPort());

        System.out.println("Servlet listening on port 8083");
        while (true) {
           Socket sock = socket.accept();
            new Dispatcher(ctx, sock).service();

            sock.close();
        }
    }

    private void init() throws Exception {
        ctx.init();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.init();
            server.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
