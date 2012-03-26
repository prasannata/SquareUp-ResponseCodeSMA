package org.prasanna.square;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorServlet extends HttpServlet
{
    private static final long serialVersionUID = -2426356058346762946L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String errorMessage = null;
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");

        if (statusCode == null)
        {
            statusCode = HttpServletResponse.SC_NOT_IMPLEMENTED;
        }

        if(servletName != null)
        {
            errorMessage += "Servlet: " + servletName;
        }
        
        if(throwable != null)
        {
            errorMessage += " caused " + throwable.getMessage();
        }
        
        response.sendError(statusCode, errorMessage);
        ResponseCodeSMA.getInstance().record(statusCode);
    }
}
