package com.ap.framework;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServletOutputStreamImpl extends ServletOutputStream {
    private ServletResponseImpl response;
    private OutputStream os;

    public ServletOutputStreamImpl(ServletResponseImpl response, OutputStream os) {
        this.response = response;
        this.os = os;
    }

    @Override
    public void write(int b) throws IOException {
        response.commit();
        os.write(b);
    }

}
