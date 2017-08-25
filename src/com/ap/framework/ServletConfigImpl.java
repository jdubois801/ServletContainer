package com.ap.framework;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class ServletConfigImpl implements ServletConfig {

    @Override
    public String getServletName() {
        return null;
    }

    @Override
    public ServletContext getServletContext() {
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
}
