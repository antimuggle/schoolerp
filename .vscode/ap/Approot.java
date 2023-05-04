package com.school.services.providers;

import jakarta.ws.rs.core.*;
import java.util.*;


public class Approot extends Application{

    private HashSet<Class<?>> classes = new HashSet<Class<?>>();
    private HashSet<Object> singletons= new HashSet<Object>();

    public Approot(){

        singletons.add(new StudentResource());
        classes.add(Student.class);
    }

    public Set<Class<?>> getClassses(){
        

        return classes; 
    }

    public Set<Object> getSingletons(){

        return singletons;
    }
}

/* 
public class Approot extends ResourceConfig{

    public Approot(){

       // register(org.glassfish.jersey.server.spi.ExternalRequestScope.class);
       // property(ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
        register(StudentResource.class);
        
    }
    }
     */

