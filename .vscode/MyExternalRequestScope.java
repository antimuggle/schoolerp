package com.school.services.providers.ExternalRequestScope;

import org.glassfish.jersey.server.spi.ExternalRequestScope;
import org.glassfish.jersey.servlet.internal.spi.RequestScopedInitializerProvider;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.spi.ExternalRequestContext;

public class MyExternalRequestScope implements ExternalRequestScope<RequestScopedInitializerProvider>{

     
    public static final ThreadLocal<InjectionManager> localthread = new ThreadLocal<InjectionManager>();

    @Override
    public MyRequestContext open(InjectionManager manager){

        localthread.set(manager);
        return new MyRequestContext(null);
    }

    @Override
    public void suspend(final ExternalRequestContext<RequestScopedInitializerProvider> c, InjectionManager manager){
        
        localthread.remove();
    }

    @Override
    public void resume(final ExternalRequestContext<RequestScopedInitializerProvider> c, InjectionManager manager){

        localthread.set(manager);
    }

    @Override
    public void close(){
        localthread.remove();
    }
    
}
