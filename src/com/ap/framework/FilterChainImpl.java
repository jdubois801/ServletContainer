package com.ap.framework;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.Servlet;


public class FilterChainImpl implements FilterChain {
    private Filter filter;
    private Servlet servlet;
    private FilterChain filterChain;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        if (filter != null) {
            filter.doFilter(servletRequest, servletResponse, filterChain);
        }
        else if (servlet != null) {
            servlet.service(servletRequest, servletResponse);
        }
        else {
            throw new ServletException("Error in filter chain: no next handler in chain.");
        }
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public FilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(FilterChain filterChain) {
        this.filterChain = filterChain;
    }
}
