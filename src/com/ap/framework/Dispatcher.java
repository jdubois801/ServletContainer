package com.ap.framework;

import com.ap.framework.http.*;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.Servlet;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

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

        FilterChain chain = buildFilterChain(req);

        HttpServletResponseImpl resp = new HttpServletResponseImpl(this);
        try {
            chain.doFilter(req, resp);
            resp.flushBuffer();
        }
        finally {
            os.close();
            is.close();
        }
    }

    private FilterChain buildFilterChain(HttpServletRequest req) throws ServletException {
        FilterChainImpl head = new FilterChainImpl();
        String requestURI = req.getRequestURI();

        // TODO: ctx should do a longest match on the req.getRequestURI() to find the servlet
        Servlet servlet = ctx.getServlet(requestURI);
        head.setServlet(servlet);

        // TODO: ctx should find filters that apply to the request URI as well

        for (Filter filter : ctx.getFilters(requestURI)) {
            FilterChainImpl next = new FilterChainImpl();
            next.setFilterChain(head);
            next.setFilter(filter);

            head = next;
        }

        return head;
    }


    public InputStream getInputStream() { return is; }
    public OutputStream getOutputStream() { return os; }
}
