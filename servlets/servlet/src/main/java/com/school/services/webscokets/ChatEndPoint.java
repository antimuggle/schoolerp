package com.school.services.webscokets;

import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.CloseReason;

public class ChatEndPoint extends Endpoint{
    private static final ConcurrentLinkedQueue<Session> peers= new ConcurrentLinkedQueue<Session>();

    @Override
    public void onOpen(Session session, EndpointConfig config){
        peers.add(session);
        session.addMessageHandler( new MessageHandler.Partial<String>() {
            @Override
            public void onMessage(String message, boolean done) {
                broadcast(session, message);
            }
        });
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        peers.remove(session);
    }

    public void broadcast(Session session, Object message){

        if(message instanceof String)
        peers.forEach(peer -> peer.getAsyncRemote().sendText(session.getId() + (String)message));
    }
}
