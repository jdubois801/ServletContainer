package com.ap.framework;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ServletRequestImpl implements ServletRequest {
    protected Dispatcher dispatcher;
    protected String characterCoding = "UTF-8";
    private ServletInputStreamImpl is = null;
    protected String protocol = null;
    protected Map<String,List<String>> parameters = new HashMap<>();

    public ServletRequestImpl(Dispatcher dispatcher) {

        this.dispatcher = dispatcher;
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
    public String getCharacterEncoding() {

        return characterCoding;
    }

    @Override
    public void setCharacterEncoding(String s) {

        characterCoding = s;
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (is == null) {
            is = new ServletInputStreamImpl(dispatcher.getInputStream());
        }
        return is;
    }

    @Override
    public String getParameter(String s) {

        List<String> slist = parameters.get(s);
        if (slist == null || slist.isEmpty()) {
            return null;
        }

        return slist.get(0);
    }

    @Override
    public Enumeration getParameterNames() {

        return Collections.enumeration(parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String s) {

        return parameters.get(s).toArray(new String[0]);
    }

    @Override
    public Map getParameterMap() {
        return Collections.unmodifiableMap(parameters);
    }

    protected void addParameter(String key, String value) {
        List<String> slist = parameters.get(key);
        if (slist == null || slist.isEmpty()) {
            slist = new ArrayList<>();
            parameters.put(key, slist);
        }

       slist.add(value);
    }

    @Override
    public String getProtocol() {

        return protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }
}
