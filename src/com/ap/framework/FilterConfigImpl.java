package com.ap.framework;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class FilterConfigImpl implements FilterConfig {
    @Override
    public String getFilterName() {
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
