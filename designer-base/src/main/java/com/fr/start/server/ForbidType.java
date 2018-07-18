package com.fr.start.server;

import javax.servlet.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class ForbidType implements Filter {
    private String message;

    public void init(FilterConfig config) throws ServletException {
        message = config.getInitParameter("msg");

        System.out.println("Filter is ready.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(message);

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.sendRedirect("/webroot/decision");
    }

    public void destroy() {

    }

}