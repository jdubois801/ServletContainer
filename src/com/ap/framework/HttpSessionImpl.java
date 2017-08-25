package com.ap.framework;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

public class HttpSessionImpl implements HttpSession {
    private long createTime = System.currentTimeMillis();
    private String id;
    private ServletContext ctx;

    public HttpSessionImpl(ServletContext ctx, String id) {
        this.ctx = ctx;
        this.id = id;
    }

    @Override
    public long getCreationTime() {
        return createTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return ctx;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
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
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Deprecated
    @Override
    public Object getValue(String s) {
        return null;
    }


    @Deprecated
    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Deprecated
    @Override
    public void putValue(String s, Object o) {

    }


    @Deprecated
    @Override
    public void removeValue(String s) {

    }

    @Deprecated
    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }


}
