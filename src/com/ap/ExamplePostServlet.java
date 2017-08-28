package com.ap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

public class ExamplePostServlet extends HttpServlet {

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

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("ExampleServlet.doPost");
        System.out.println("query string (raw) = " + req.getQueryString());

        String contentType = req.getHeader("Content-Type");
        System.out.println("content type = " + contentType);

        if ("application/x-www-form-urlencoded".equals(contentType)) {
            Map<String, String> params = parseFormUrlEncoded(req);

            System.out.println("value1 = " + params.get("value1"));
            System.out.println("value2 = " + params.get("value2"));

        } else if (contentType.startsWith("multipart/form-data")) {
            Map<String, String> params = parseFormData(req);

            System.out.println("value1 = " + params.get("value1"));
            System.out.println("value2 = " + params.get("value2"));

        } else {
            System.out.println("unrecognized content type = " + contentType);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<String, String> parseFormUrlEncoded(HttpServletRequest req) throws IOException, ServletException {
        Map<String, String> result = new HashMap<>();

        String body = readWholeBody(req);

        for (String nvp : body.split("&")) {
            String[] parts = nvp.split("=");
            result.put(parts[0], parts[1]);
        }

        return result;
    }

    private String readWholeBody(HttpServletRequest req) throws IOException, ServletException {
        String result = null;

        String contentLength = req.getHeader("Content-Length");
        System.out.println("content-length = " + contentLength);

        if (contentLength != null && !contentLength.isEmpty()) {
            try {
                int l = Integer.parseInt(contentLength);
                byte[] body = new byte[l];
                req.getInputStream().read(body);
                result = URLDecoder.decode(new String(body), "US-ASCII");  // character coding from RFC 1738
            } catch (NumberFormatException e) {
                throw new ServletException("Cannot read content length header", e);
            }
        }

        return result;
    }

    private Map<String, String> parseFormData(HttpServletRequest req) throws IOException, ServletException {
        Map<String,String> result = new HashMap<>();
        InputStream is = req.getInputStream();

        // parse sections according to RFC 2045
        while (hasNextSection(is)) {
            Map<String, String> smap = readSection(is);

            if (smap.isEmpty()) {
                break;
            }

            if (smap.get("FieldValue") != null) {
                // simple form field
                result.put(smap.get("FieldName"), smap.get("FieldValue"));
            }
        }

        return result;
    }

    private Map<String,String> readSection(InputStream is) throws IOException {
        Map<String,String> result = new HashMap<>();

        String line1 = readLine(is);  // the boundary marker

        if (line1.trim().endsWith("--")) {
            // empty (last) section
            return result;
        }

        Map<String,String> headerMap = parseContentHeaders(is);

        // if submitted section is a simple form value, dispositionMap will be empty
        // if submitted section contains a file, there will be other headers and contents in the disposition map
        Map<String,String> dispositionMap = parseContentDisposition(headerMap.get("Content-Disposition"));

        // now read bytes until you get a "\r\n--"
        String rawContent = readRawBytes(is);

        // TODO: look for a Content-Type header that indicates the character coding to use for "rawContent"

        result.putAll(headerMap);
        result.putAll(dispositionMap);

        String simpleName = dispositionMap.get("name");

        if (simpleName != null) {
            result.put("FieldName", simpleName.replaceAll("\"", "") );
            result.put("FieldValue", rawContent);
        }

        // TODO: what if the submitted data was a file?

        return result;
    }

    private Map<String,String> parseContentDisposition(String line) {
        Map<String,String> result = new HashMap<>();

        String[] items = line.split(";\\s*");
        for (String item : items) {
            String[] parts = item.split("=");
            if (parts.length < 2) {
                result.put("Content-Disposition", parts[0]);
            }
            else {
                result.put(parts[0], parts[1].trim());
            }
        }

        return result;
    }

    private String readRawBytes(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        boolean foundLF = false;
        boolean foundOneHyphen = false;

        while(true) {
            int c = is.read();

            if (c == -1) {
                break;
            }

            sb.append((char)c);

            // low-rent state machine
            if (foundLF) {
                if (foundOneHyphen) {
                    if (c == '-') {
                        break;
                    }
                    else {
                        foundOneHyphen = false;
                        foundLF = false;
                    }
                }
                else if (c == '-') {
                    foundOneHyphen = true;
                }
                else {
                    foundLF = false;
                }
            }
            else {
                if (c == '\n') {
                    foundLF = true;
                }
            }
        }

        return sb.toString().substring(0, sb.length()-2).trim();
    }

    private boolean hasNextSection(InputStream is) throws IOException {
        int c = is.read();
        int c2 = -1;

        if (c == '-') {
            c2 = is.read();
        }

        return c == '-' && c2 == '-';
    }

    // read lines until a blank line is found
    private Map<String,String> parseContentHeaders(InputStream is) throws IOException {
        Map<String,String> result = new HashMap<>();

        while(true) {
            String line = readLine(is);
            if (line != null) {
                line = line.trim();
            }

            if (line == null || line.isEmpty()) {
                break;
            }

            String[] parts = line.split(":\\s*");
            result.put(parts[0], parts[1]);
        }

        return result;
    }

    private String readLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();

        while (true) {
            char c = (char)is.read();
            sb.append(c);  // without character code conversion!

            if (c == '\n') {
                break;
            }
        }

        return sb.toString();
    }
}