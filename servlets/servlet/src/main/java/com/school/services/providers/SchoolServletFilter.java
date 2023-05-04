package com.school.services.providers;

import org.glassfish.jersey.server.ContainerRequest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;


@Provider
public class SchoolServletFilter implements ContainerRequestFilter{
    
    @Override
    public void filter(ContainerRequestContext context){
         
    }
    
}
