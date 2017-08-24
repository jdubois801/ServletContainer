package com.ap.framework;

import com.ap.framework.http.*;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.Servlet;


public class Dispatcher {
    private ServletContextImpl ctx;
    private Socket sock;
    private InputStream is;
    private OutputStream os;

    public Dispatcher(ServletContextImpl ctx, Socket sock) {
        this.ctx = ctx;
        this.sock = sock;
    }

    void service() throws IOException, ServletException {
        is = sock.getInputStream();
        os = sock.getOutputStream();

        System.out.println("Dispatcher handling request");

        HttpServletRequestImpl req = new HttpServletRequestImpl(this);

        // TODO: ctx should do a longest match on the req.getRequestURI() to find the servlet
        // TODO: ctx should find filters that apply to the request URI as well
        
        Servlet servlet = ctx.getServlet("name");
        //System.out.println("servlet = " + servlet);

        HttpServletResponseImpl resp = new HttpServletResponseImpl(this);
        try {
            servlet.service(req, resp);
            resp.flushBuffer();
        }
        finally {
            os.close();
            is.close();
        }
    }

    public InputStream getInputStream() { return is; }
    public OutputStream getOutputStream() { return os; }
}
