package com.school.services.providers;

import org.glassfish.jersey.servlet.internal.spi.RequestScopedInitializerProvider;
import org.glassfish.jersey.servlet.internal.spi.RequestContextProvider;
import org.glassfish.jersey.server.spi.RequestScopedInitializer; 

public class ServletRequestScopedInitializerProvider implements RequestScopedInitializerProvider{

    @Override
    public RequestScopedInitializer get(RequestContextProvider provider){
        
        return new ServletRequestScopedInitializer();
    }
    
}
