package com.studinstructor.services;

import java.sql.*;
import jakarta.annotation.*;
import jakarta.ejb.*;

import javax.naming.*;
import javax.sql.*;

@Stateful(name="stud")
public class StudentService implements studentservices{
    
@Resource(name ="jdbc/schoolproj")
private  DataSource dSource;

Context context;

@PostConstruct
public void init(){
    try{
        

        context = new InitialContext();
    }
    catch(NamingException ex){
        ex.printStackTrace();
    }

}
public void deploued(){
    try{
        java.sql.Connection con = dSource.getConnection();

    String sql = "INSERT INTO ADMIN(Name,Occupation) VALUES('Oyelowo','sweeper')";
    PreparedStatement  stmt = con.prepareStatement(sql);
          stmt.execute();
    }

    catch(SQLException ex){
        ex.printStackTrace();
    }         

}

}
           
           
 
