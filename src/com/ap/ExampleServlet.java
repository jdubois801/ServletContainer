package com.ap;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ExampleServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("ExampleServlet.doGet");

        // create a session
        req.getSession();

        System.out.println("request URI = " + req.getRequestURI());
        System.out.println("path info = " + req.getPathInfo());
        System.out.println("query string (raw) = " + req.getQueryString());
        System.out.println("e param = " + req.getParameter("e"));

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("Connection", "close");
        resp.setIntHeader("Keep-Alive", 300);
        resp.getWriter().println("Example Servlet Response: OK");
    }
}
