package org.prasanna.square;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LocationServlet extends HttpServlet
{
    private static final long serialVersionUID = -7924660090483081542L;
    private List<Integer> locationIdList = Collections.synchronizedList(new ArrayList<Integer>());
    private final Pattern pattern = Pattern.compile("/([0-9]+)/{0,1}");
    private ResponseCodeSMA responseCodeSMA;

    public LocationServlet()
    {
        responseCodeSMA = ResponseCodeSMA.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        recordResponse(response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Integer locationId = getLocationId(request.getPathInfo());

        if (locationId != null)
        {
            if (locationIdList.contains(locationId) == false)
            {
                locationIdList.add(locationId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Location " + locationId + " already exists");
            }
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getPathInfo() + " not found.");
        }

        recordResponse(response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        Integer locationId = getLocationId(request.getPathInfo());

        if (locationId != null && locationIdList.contains(locationId))
        {
            locationIdList.remove(locationId);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getPathInfo() + " not found.");
        }

        recordResponse(response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Integer locationId = getLocationId(request.getPathInfo());

        if (locationId != null && locationIdList.contains(locationId))
        {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getPathInfo() + " not found.");
        }

        recordResponse(response);
    }

    private Integer getLocationId(String pathInfo)
    {
        Integer id = null;

        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.find() == true)
        {
            id = Integer.parseInt(matcher.group(matcher.groupCount()));
        }

        return id;
    }

    private void recordResponse(HttpServletResponse response)
    {
        responseCodeSMA.record(response.getStatus());
    }
}
