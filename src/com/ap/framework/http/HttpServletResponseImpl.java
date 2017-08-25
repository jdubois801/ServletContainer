package com.ap.framework.http;

import com.ap.framework.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Map;

public class HttpServletResponseImpl extends ServletResponseImpl implements HttpServletResponse {
    private OutputStream os;
    private boolean flushed = false;
    protected Map<String,List<String>> headers = new HashMap<>();
    private int statusCode;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public HttpServletResponseImpl(Dispatcher dispatcher) {
        super(dispatcher);

        this.os = dispatcher.getOutputStream();

        setHeader("X-Powered-By", "Framework");
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public String encodeURL(String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {
        flushed = true;
    }

    @Override
    public void sendError(int i) throws IOException {
        flushed = true;

    }

    @Override
    public void sendRedirect(String s) throws IOException {
        flushed = true;
    }

    @Override
    public boolean containsHeader(String s) {

        return headers.containsKey(s);
    }

    @Override
    public void setDateHeader(String s, long l) {
        setHeader(s, String.valueOf(l));
    }

    @Override
    public void addDateHeader(String s, long l) {
        addHeader(s, String.valueOf(l));
    }

    @Override
    public void setHeader(String s, String s1) {
        List<String> slist = headers.get(s);
        if (slist == null) {
            slist = new ArrayList<>();
            headers.put(s, slist);
        }
        slist.clear();
        slist.add(s1);
    }

    @Override
    public void addHeader(String s, String s1) {
        List<String> slist = headers.get(s);
        if (slist == null) {
            slist = new ArrayList<>();
            headers.put(s, slist);
        }

        slist.add(s1);
    }

    @Override
    public void setIntHeader(String s, int i) {
        setHeader(s, String.valueOf(i));
    }

    @Override
    public void addIntHeader(String s, int i) {
        addHeader(s, String.valueOf(i));
    }

    @Override
    public void setStatus(int i) {
        statusCode = i;
    }

    @Deprecated
    @Override
    public void setStatus(int i, String s) {
        setStatus(i);
    }

    @Override
    public void reset() {
        super.reset();
        statusCode = 0;
        headers.clear();
    }

    @Override
    protected void commit() throws IOException {
        if (!committed) {
            super.commit();  // only sets the flag

            writeResponseHeader();
            byte[] barry = baos.toByteArray();
            os.write(barry);
        }
    }

    private void writeResponseHeader() throws IOException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos, getCharacterEncoding()));
        pw.println("HTTP/1.x " + String.valueOf(statusCode) + "OK");

        // now loop through the headers
        for (String key : headers.keySet()) {
            List<String> slist = headers.get(key);
            for (String str : slist) {
                pw.println(key + ": " + str);
            }
        }
        pw.println();
        pw.close();
    }

    public void attachSession(HttpSession session) {
        setHeader("Set-Cookie", "JSESSIONID=" + session.getId());
    }
}
