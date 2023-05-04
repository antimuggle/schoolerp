package com.school.services.providers;

import org.glassfish.jersey.servlet.internal.spi.RequestContextProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;

public class ServletContextProvider implements RequestContextProvider {


    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Override
    public HttpServletRequest getHttpServletRequest(){

        return request;
    }

    @Override
    public HttpServletResponse getHttpServletResponse(){

        return response;
    }
    
}
