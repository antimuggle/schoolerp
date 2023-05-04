package com.studinstructor.data.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class Course implements Serializable{
    private String name = null;
    private String deptName= null;
    private User hod = null;
    private List<Unit> unitlist= null;
    private Connection con = null;
    private ResultSet set = null;

    public Course(){};
    public Course(String name, Connection con)throws IllegalArgumentException{
        this();
        if(name == null || name == "") throw new IllegalArgumentException();
        if(con == null) throw new IllegalArgumentException();
        this.name = name;
        this.con = con;
    }

    private void initCourseRowSet(){
        if( set == null){
            if(this.name!=null || this.name != ""){
                try{
                    PreparedStatement stmt = con.prepareStatement("select COURSE.Course_Name, COURSE.Dept_Name, dept.dept_Head"+ 
                                                                    "from COURSE JOIN DEPARTMENT as dept ON COURSE.Dept_Name=dept.Dept_Name"+ 
                                                                        "where COURSE.Course_Name=" + this.name, ResultSet.CONCUR_READ_ONLY,
                                                                          ResultSet.CLOSE_CURSORS_AT_COMMIT );
                    if(stmt != null) 
                        set = stmt.executeQuery();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public String getCourseName(){      
        initCourseRowSet();
        try{
            set.first();
            if(set.getString("Course_Name")!= null){
                this.deptName = set.getString("Course_Name");
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }


        return this.deptName;
    }

    public List<Unit> getUnitList(){
        if(unitlist == null){
            try{
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM unit_List", ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet set = stmt.executeQuery();
                set.beforeFirst();
                while(set.next()){
                    unitlist.add(new Unit(set.getString("Unit_Id"), this.con));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        

        }
        return this.unitlist;
    }

    public User getHod(){

       initCourseRowSet();
       try{
            if(set.getInt("dept_Head")!= 0){
                this.hod =(User) new Instructor(set.getInt("dept_Head"),this.con);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

       return this.hod;
    }
    
    
    
}
