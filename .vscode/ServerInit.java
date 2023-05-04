package com.school.services.providers;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.ws.rs.core.UriBuilder;

import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.init.JerseyServletContainerInitializer;
import org.glassfish.jersey.servlet.internal.ServletContainerProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;

public class ServerInit {


    private static final String JERSEY_SERVLET_CONTEXT_PATH = getBaseURI().toString() + "/resources";
    
    private static URI getBaseURI() {
      
        return UriBuilder.fromUri("http://localhost").port(8080).path("/servlet").build();
    }
    
    public static final URI BASE_URI = getBaseURI();
    
    public static void main(String[] args) throws IOException {
            // Create HttpServer and register dummy "not found" HttpHandler

    HttpServer httpServer = GrizzlyWebContainerFactory.create(JERSEY_SERVLET_CONTEXT_PATH, ServletContainer.class);

            
    
    // Initialize and register Jersey Servlet


    
    httpServer.start();
    WebappContext context = new WebappContext("WebappContext", JERSEY_SERVLET_CONTEXT_PATH);
    ServletRegistration registration = context.addServlet("ServletContainer", ServletContainer.class);
    
    
    Set<Class<?>> dirclasses = new HashSet<Class<?>>();
    dirclasses.add(Schoolapp.class);
    dirclasses.add(Student.class);
    dirclasses.add(StudentResource.class);
    dirclasses.add(StudentClient.class);

    System.out.println("trying to deploy");
    context.deploy(httpServer);

    try {JerseyServletContainerInitializer jerseyinit = new JerseyServletContainerInitializer();
        
        jerseyinit.onStartup(dirclasses, context);
        } 
    
        catch(ServletException ex){
          System.out.println("threre has been a servletException");
        }
    
    System.in.read();


    httpServer.shutdown();
        }
    
    
}
