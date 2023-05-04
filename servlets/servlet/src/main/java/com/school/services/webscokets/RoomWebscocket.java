package com.school.services.webscokets;

import jakarta.ejb.Stateful;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.annotation.*;

import java.util.HashSet;
import java.util.Set;

import javax.naming.*;

import org.glassfish.grizzly.http.io.BinaryNIOInputSource;

@ServerEndpoint(value="/rooms/chat/{roomId}")
@Stateful(name="chat")
public class RoomWebscocket {

    private InitialContext context;
    private Set<String> peers= new HashSet<String>();

    @PostConstruct
    public void init(){
        try{
        

            context = new InitialContext();
        }
    
        catch(NamingException ex){
    
            ex.printStackTrace();
    
        }
    }  
    
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        peers.add(session.getId().toString());
    }
    
    @OnClose
    public void onClose(Session session) {
     
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println(session.getId());
        error.printStackTrace();
    }
}
