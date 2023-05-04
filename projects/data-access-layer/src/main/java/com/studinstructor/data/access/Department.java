package com.studinstructor.data.access;

import java.io.Serializable;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Department implements Serializable{
   private String deptname = null;
   private Instructor depthead = null;
   private ResultSet deptrowset = null;
   private ResultSet courses = null;
   private List<Course> courseList = null;
   private Connection con = null;

   private Department(){};
 
   public Department(String name, Connection con) throws IllegalArgumentException{
      this();
      if(name == null || name == "") throw new IllegalArgumentException();
      if(con == null) throw new IllegalArgumentException();
      this.deptname = name;
      this.con = con;
   }

   private void initDeptRowset(){
      if(this.deptrowset == null){
         if(this.deptname!=null){
            try{
               PreparedStatement stmt = con.prepareStatement("select * from COURSE where Dept_Name" + deptname, ResultSet.CONCUR_READ_ONLY,
                                                                       ResultSet.CLOSE_CURSORS_AT_COMMIT);
               deptrowset = stmt.executeQuery();
            }
            catch(SQLException ex){
               ex.printStackTrace();
            }
         }
      }
   }

   private void initCoursesRowset(){
      if(this.courses == null){
         if(this.deptname!=null){
            try{
               PreparedStatement stmt = con.prepareStatement("select * from DEPARTMENT where Dept_Name="+deptname, ResultSet.CONCUR_READ_ONLY,
                                                                       ResultSet.CLOSE_CURSORS_AT_COMMIT );
               courses = stmt.executeQuery();
            }
            catch(SQLException ex){
               ex.printStackTrace();
            }
         }
      }
   }

   public User getDeptHead(){
      initDeptRowset();
      try{
         deptrowset.first();
         this.depthead = new Instructor(deptrowset.getInt("Dept_Head"), this.con);
      }
      catch(SQLException ex){
         ex.printStackTrace();
      }
      return this.depthead;  
   }

   public String getDeptName(){

      return this.deptname;
   }

   public List<Course> getCourseList(){

      initCoursesRowset();
      try{
         courses.beforeFirst();
          this.courseList = new ArrayList<Course>();
         while(courses.next()){
            this.courseList.add(new Course(courses.getString("Course_Name"), this.con));
         }
      }
      catch(SQLException ex){
         ex.printStackTrace();
      }

    return this.courseList;
   }   
}

