package com.school.services.providers;

import jakarta.ws.rs.core.*;
import java.util.*;


public class Approot extends Application{

    private HashSet<Class<?>> classes = new HashSet<Class<?>>();
    private HashSet<Object> singletons= new HashSet<Object>();

    public Approot(){

        singletons.add(new StudentResource());
        classes.add(Student.class);
        classes.add(StudentClient.class);
        classes.add(studentservlet.class);
    }

    public Set<Class<?>> getClassses(){
        

        return classes; 
    }

    public Set<Object> getSingletons(){

        return singletons;
    }
}