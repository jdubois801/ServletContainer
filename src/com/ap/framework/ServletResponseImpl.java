package com.ap.framework;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class ServletResponseImpl implements ServletResponse {
    private OutputStream os;
    private String characterCoding = "UTF-8";
    private int bufferSize = 512;
    private String contentType = null;
    private Locale locale = null;
    private boolean gotOutputStream = false;
    private boolean gotWriter = false;
    protected boolean committed = false;
    protected ServletOutputStreamImpl outputStream = null;
    private PrintWriter pw;
    private OutputStream out;

    public ServletResponseImpl(Dispatcher dispatcher) {

        this.os = dispatcher.getOutputStream();
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
    public ServletOutputStream getOutputStream() throws IOException {
        if (gotWriter) {
            throw new IllegalStateException();
        }
        gotOutputStream = true;

        // TODO: this stream needs a buffer!
        outputStream = new ServletOutputStreamImpl(this, os);
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (gotOutputStream) {
            throw new IllegalStateException();
        }
        gotWriter = true;

        outputStream = new ServletOutputStreamImpl(this, os);
        pw = new PrintWriter(outputStream);
        return pw;
//        return new PrintWriter(new OutputStreamWriter(outputStream, characterCoding));
    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public String getContentType() {

        return contentType;
    }

    @Override
    public void setContentType(String s) {
        contentType = s;
    }

    @Override
    public void setBufferSize(int i) {
        if (isCommitted()) {
            throw new IllegalStateException();
        }
        bufferSize = i;
    }

    @Override
    public int getBufferSize() {

        return bufferSize;
    }

    @Override
    public void flushBuffer() throws IOException {
        commit();
        if (pw != null) {
            pw.flush();
        }
        else if (out != null) {
            out.flush();
        }

        // might be rundant if "pw" or "out" propogates the flush
        if (outputStream != null) {
            outputStream.flush();
        }
    }

    @Override
    public void resetBuffer() {

    }

    protected void commit() throws IOException {
        committed = true;
    }

    @Override
    public boolean isCommitted() {

        return committed;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {

        return locale;
    }
}
