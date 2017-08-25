package com.ap.framework;

import javax.servlet.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.TreeSet;

import com.ap.*;

public class ServletContextImpl implements ServletContext {
    private Properties props = new Properties();
    private Map<String,Filter> filterMap = new HashMap<>();
    private Map<String,Servlet> servletMap = new HashMap<>();
    private int port = 8080;
    private SessionManager sessionManager = null;

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public ServletContext getContext(String s) {
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String s) {
        return null;
    }

    @Override
    public Set getResourcePaths(String s) {
        return null;
    }

    @Override
    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }



    @Override
    public Enumeration getServlets() {
        return null;
    }

    @Override
    public Enumeration getServletNames() {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(Exception e, String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public String getServerInfo() {
        return null;
    }

    @Override
    public String getInitParameter(String s) {
        return null;
    }

    @Override
    public Enumeration getInitParameterNames() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getServletContextName() {
        return null;
    }

    public int getPort() {
        return port;
    }

    @Override
    public Servlet getServlet(String s) throws ServletException {

        // TODO: match the URL wildcard notation in the key with the context pruned requestURI

        // DEBUGGING: just return the first configured servlet
        String servletpath = servletMap.keySet().iterator().next();
        return servletMap.get(servletpath);
    }

    public List<Filter> getFilters(String requestUri) {
        List<Filter> results = new ArrayList<>();

        String suffix = requestUri;
        String contextPath = getContextPath();
        if (contextPath == null || contextPath.isEmpty()) {
            contextPath = "";
        }

        if (requestUri.startsWith(contextPath)) {
            suffix = requestUri.substring(contextPath.length());
            System.out.println("getFilters suffix = " + suffix);
        }

        for (String key : filterMap.keySet()) {
            // TODO: match the URL wildcard notation in the key with the context pruned requestURI

            results.add(filterMap.get(key));  // DEBUGGING temporary: add all configured filters to result list
        }

        return results;
    }

    // initialize the context
    void init() throws Exception {
        FileInputStream fis = new FileInputStream("application.properties");
        props.load(fis);
        fis.close();

        port = Integer.parseInt(props.getProperty("server.port"));

        Map<String,Filter> filters = new HashMap<>();
        Map<String,Servlet> servlets = new HashMap<>();

        // force the properties name list into a sorted set of String keys
        TreeSet<String> keys = new TreeSet<String>();
        for (Object o : props.keySet()) {
            keys.add((String)o);
        }

        for (String key : keys) {
            String[] components = key.split("\\.");

            if (components[0].startsWith("servlet")) {

                if (components.length > 1) {
                    if ("class".equals(components[1])) {
                        Class clz = Class.forName((String) props.getProperty(key));
                        Servlet servlet = (Servlet) clz.newInstance();
                        servlets.put(components[0], servlet);

                        // TODO: init the instance
                        ServletConfig config = new ServletConfigImpl();
                        servlet.init(config);
                    }
                    else if ("path".equals(components[1])) {
                        Servlet servlet = servlets.get(components[0]);
                        servletMap.put(props.getProperty(key), servlet);
                    }

                }
            }
            else if (components[0].startsWith("filter")) {

                if (components.length > 1) {
                    if ("class".equals(components[1])) {
                        Class clz = Class.forName(props.getProperty(key));
                        Filter filter = (Filter) clz.newInstance();
                        filters.put(components[0], filter);

                        // TODO: init the instance
                        FilterConfig config = new FilterConfigImpl();
                        filter.init(config);
                    }
                    else if ("path".equals(components[1])) {
                        Filter filter = filters.get(components[0]);
                        filterMap.put(props.getProperty(key), filter);
                    }

                }
            }
        }
    }

    public SessionManager getSessionManager() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }
}
