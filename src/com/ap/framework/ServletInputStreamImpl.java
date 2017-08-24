package com.ap.framework;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;

public class ServletInputStreamImpl extends ServletInputStream {
    private InputStream is;

    public ServletInputStreamImpl(InputStream is) {
        this.is = is;
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }
}
