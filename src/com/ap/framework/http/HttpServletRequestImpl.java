package com.ap.framework.http;

import com.ap.framework.ServletRequestImpl;
import com.ap.framework.Dispatcher;
import com.ap.framework.ServletInputStreamImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class HttpServletRequestImpl extends ServletRequestImpl implements HttpServletRequest {
    private String method = null;
    private String resource = null;
    private String pathinfo;
    private Map<String,List<String>> headers = new HashMap<>();
    private String queryString = null;

    public HttpServletRequestImpl(Dispatcher dispatcher) throws IOException {
        super(dispatcher);

        // read first line of HTTP transaction
        parseProtocolRequest();

        parseResource();

        // if HTTP/1.0 protocol or later, parse remainder of header
        if (protocol.indexOf("0.9") < 0) {
            parseHeaders();
        }
    }

    @Override
    public String getAuthType() {
        // TODO: parse the Authorization header
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        // TODO: parse cookies
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {

        String value = getHeader(s);
        if (value == null) {
            return -1;
        }

        return Long.parseLong(value);
    }

    @Override
    public String getHeader(String s) {

        List<String> slist = headers.get(s);
        if (slist == null || slist.isEmpty()) {
            return null;
        }

        return slist.get(0);
    }

    @Override
    public Enumeration getHeaders(String s) {

        return Collections.enumeration(headers.get(s));
    }

    @Override
    public Enumeration getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    @Override
    public int getIntHeader(String s) {

        String value = getHeader(s);
        if (value == null) {
            return -1;
        }

        return Integer.parseInt(value);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return pathinfo;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return resource;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private String readFirstLine() throws IOException {
        StringBuffer sb = new StringBuffer();
        ServletInputStream is = getInputStream();

        while (true) {
            char c = (char)is.read();
            sb.append(c);  // without character code conversion!

            if (c == '\n') {
                break;
            }
        }

        return sb.toString();
    }

    private void parseProtocolRequest() throws IOException {
        String[] components = readFirstLine().split(" ", 3);

        method = components[0].toUpperCase();
        resource = components[1];

        if (components.length > 2) {
            protocol = components[2];
        }
        else {
            protocol = "HTTP/0.9";
        }
    }

    private void parseResource() throws UnsupportedEncodingException {
        String src = resource;
        int findex = src.indexOf('#');

        if (findex > 0) {
            src = resource.substring(0, findex);
            resource = src;
        }

        int pindex = src.indexOf(';');
        int qindex = src.indexOf('?');

        // TODO: Wrong!  path info comes after the servlet path in the URI

        if (pindex > 0 && qindex > 0) {
            resource = pathinfo = src.substring(0, pindex);
            parseRFC1945Params(src.substring(pindex+1, qindex));
            parseQueryParams(src.substring(qindex+1));
        }
        else if (pindex > 0) {
            resource = pathinfo = src.substring(0, pindex);
            parseRFC1945Params(src.substring(pindex+1));
        }
        else if (qindex > 0) {
            resource = pathinfo = src.substring(0, qindex);
            parseQueryParams(src.substring(qindex+1));
        }

        if (resource.endsWith("/")) {
            resource = resource.substring(0, resource.length()-1);
        }
    }

    private void parseRFC1945Params(String src) {
        String[] params = src.split(";");

        // TODO: now what? RFC1945 isn't clear what goes in a param
    }

    private void parseQueryParams(String src) throws UnsupportedEncodingException {

        queryString = src;

        if (src != null && !src.isEmpty()) {
            for (String qp : src.split("&")) {
                String[] components = qp.split("=", 2);
                addParameter(components[0], URLDecoder.decode(components[1], "UTF-8"));
            }
        }
    }

    private void parseHeaders() throws IOException {

        // loop on reading lines until one comes up empty
        while (true) {
            String line = readFirstLine();

            if (line.charAt(0) == '\r' || line.charAt(0) == '\n') {
                break;
            }
            String[] components = line.split(":",2);

            List<String> slist = headers.get(components[0]);
            if (slist == null) {
                slist = new ArrayList<>();
                headers.put(components[0], slist);
            }
            slist.add(components[1].trim());
        }
    }
}
