package com.school.services.webscokets;

import java.util.*;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;;

public class ServerConfig implements ServerApplicationConfig{
    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpoints){
        
        Set<ServerEndpointConfig> configs = new HashSet<ServerEndpointConfig>();
        for(Class<? extends Endpoint> endpointClass : endpoints){
           if(endpointClass.equals(ChatEndPoint.class)){

                ServerEndpointConfig serverEndpointConfig = ServerEndpointConfig.Builder.create(endpointClass,"/chat/{room}").build();
                configs.add(serverEndpointConfig);

            }
        }
        return configs;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> classes){
        Set<Class<?>> configs = new HashSet<Class<?>>();
        
        for(Class<?> endpointClass : classes){
         if( endpointClass.isAnnotationPresent(ServerEndpoint.class)){
               configs.add(endpointClass);
        
            }
        }
        return configs;
    }
    
    
}
