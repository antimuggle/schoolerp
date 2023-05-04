package com.school.services.ejbs;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.*;

@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "destinationType",
        propertyValue = "jakarta.jms.Topic"), @ActivationConfigProperty(propertyName = "destinationLookup",
        propertyValue = "jms/schoolprojRoomDest")
        })
public class RoomMDB implements MessageListener{


    @Override
    public void onMessage(Message message) {
        
    }
    
}
