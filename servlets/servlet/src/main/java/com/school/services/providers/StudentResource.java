package com.school.services.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.*;

@Path("/Students")
public class StudentResource{

    private HashMap<Integer,Student> studlist = new HashMap<Integer,Student>();
    private int counter = 0;
    public StudentResource(){

    }


    @Path("/{id}")
    @GET
    public void getStudent(@PathParam("id") int id, @Context HttpServletRequest request,
                                                      @Context HttpServletResponse response) 
                                                       throws WebApplicationException{

        studlist.put(1, new Student("Dedan", "Kimathi"));
        Student stud = studlist.get(1);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter();){
        
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<title>Servlet studentservlet</title>");
                        out.println("<p>i'm here</p>");
                        out.println("<body>");
                        out.println("<h1>"+ request.getContextPath() +"</h1>");
                        out.println("<h1>"+ stud.firstname+"</h1>");
                        out.println("<h1>"+stud.lastname+"</h1>");
                        out.println("</body>");
                        out.println("</html>");
    
    }
        catch(IOException ex){
                            throw new WebApplicationException();
                        }
            
        /* 
        return Response.ok(Entity.xml(xml)).build();
        
    }

    @POST
    @Consumes("application/xml")
    public Response setStudent(InputStream is) throws WebApplicationException{

        String firstname = null;
        String lastname = null;
        try{

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();

            for(int i = 0; i < nodes.getLength(); i++){

                Element element = (Element) nodes.item(i);
                if(element.getTagName().equals("firstname")){
                    firstname = element.getTextContent();
                }
                else if (element.getTagName().equals("lastname")){

                    lastname = element.getTextContent();
                }
            }

            counter+=1;
            Student stud = new Student(firstname, lastname);
            studlist.put(counter,stud);

        }
        catch(Exception c){

            c.printStackTrace();
        }

  
        return Response.created(URI.create("http://localhost:8080/resources/Students/"+ counter)).build();
    
    
    */

    }

}