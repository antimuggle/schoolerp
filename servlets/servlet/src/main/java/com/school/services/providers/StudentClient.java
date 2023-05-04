package com.school.services.providers;

import jakarta.ws.rs.client.*;
import org.glassfish.jersey.client.ClientConfig;


public class StudentClient {

    private static ClientConfig clientConfig = new ClientConfig();
    private final String BASE_URI = "http://localhost:8080/servlet/resources";

    public int testStudentResponse(String firstname, String lastname, String username){
        
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(BASE_URI).path("/Students");
        String xml = "<student>"
                         +"<firstname>"+ firstname + "</firstname>"
                         +"<lastname>"+ lastname + "</lastname>"
                         +"<username>"+ username + "</username>"
                    +"</student>";

                    Invocation.Builder invocationBuilder =  target.request();
                    jakarta.ws.rs.core.Response response = invocationBuilder.post(Entity.xml(xml));
        int stat = response.getStatus();
            return stat;
    }
    
}
