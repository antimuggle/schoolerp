package com.school.services.providers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


@WebServlet(name="myservlet", urlPatterns ={"/suki"})
public class studentservlet extends HttpServlet{
   


    protected void processRequest(HttpServletRequest request, HttpServletResponse response){



        
    /* 
        
        RequestDispatcher dp = request.getRequestDispatcher("/resources/Students/23");
        if(dp!= null){
        try{
            dp.include(request, response);
            dp.forward(request, response);
        }
        catch(ServletException ex){

            ex.printStackTrace();
        }
        catch(IOException io){
            io.printStackTrace();
        }
    }
    else{
    

    }
     */


       /*  try{
            try(PrintWriter out = response.getWriter()){

                /* StudentClient client = new StudentClient();
        
                    if(client.testStudentResponse("Bill", "Burke", "nomnom")==201){
                 
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<title>Servlet studentservlet</title>");
                        out.println("<p>i'm here</p>");
                        out.println("<body>");
                        out.println("<h1>"+ this.getServletContext().getContextPath() +"</h1>");
                        out.println("<h1>"+ dp.FORWARD_CONTEXT_PATH +"</h1>");
                        out.println("<h1>"+ dp.FORWARD_PATH_INFO +"</h1>");
                        out.println("<h1>"+ dp.FORWARD_REQUEST_URI +"</h1>");
                        out.println("</body>");
                        out.println("</html>");
        }
        }
        catch(IOException io){
            io.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        
        
     
            /*
            }
            
            else{

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<title>Servlet studentservlet</title>");
                out.println("<p>too bad good luck next time</p>");
                out.println("<body>");
                out.println("<h1>"+ request.getContextPath() +"</h1>");
                out.println("</body>");
                out.println("</html>");
            }
            */
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        processRequest(request, response);
    }

}
