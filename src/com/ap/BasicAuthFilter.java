package com.ap;

import javax.servlet.*;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ap.framework.PrincipalImpl;

public class BasicAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        HttpSession session = req.getSession();

        if ( session.getAttribute("AUTHORIZATION_PRINCIPAL") == null) {

            String value = req.getHeader("Authorization");

            if (value != null && !value.isEmpty()) {

                if (value.startsWith("Basic ")) {
                    value = value.substring(6);
                    String decoded = new String(Base64.getDecoder().decode(value), "UTF-8");
                    String parts[] = decoded.split(":", 2);

                    if (!verify(parts[0], parts[1])) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }

                    PrincipalImpl principal = new PrincipalImpl(parts[0]);
                    req.setAttribute("AUTHORIZATION_PRINCIPAL", principal);

                    session.setAttribute("AUTHORIZATION_PRINCIPAL", principal);

                }
                // if no Basic auth header, just go on

                // success - go on with filter chain
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private boolean verify(String username, String password) {

        // TODO: actually do a verification
        return true;
    }
}
