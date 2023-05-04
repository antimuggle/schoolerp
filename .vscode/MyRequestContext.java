package com.school.services.providers.ExternalRequestScope;

import org.glassfish.jersey.server.spi.ExternalRequestContext;
import org.glassfish.jersey.servlet.internal.spi.RequestScopedInitializerProvider;

public class MyRequestContext extends ExternalRequestContext<RequestScopedInitializerProvider>{
    

    MyRequestContext(RequestScopedInitializerProvider contextinitializer){
        super(contextinitializer);
    }

    @Override
    public RequestScopedInitializerProvider getContext(){
        return super.getContext();
    }

    
}
