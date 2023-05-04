package com.studinstructor.data.access;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.Serializable;
import java.time.LocalDate;
import java.sql.Connection;

public class Semester implements Serializable{
    private int semId = 0;
    private LocalDate start = null;
    private LocalDate end = null;
    private Course course = null;
    private Connection con = null;
    private ResultSet semrowset = null;

    public Semester(int semId, Connection con){

        this.semId = semId;
        this.con = con;
    }
    
    private void initSemesterRowSet(){
        if(this.con != null && this.semrowset == null){

                try{
                    PreparedStatement stmt = this.con.prepareStatement("select * from v_seminfo where semId=" +this.semId,
                                                                ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                    this.semrowset = stmt.executeQuery();

            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }

    }
    
    public int getSemId(){
        return this.semId;
    }

    public LocalDate getStart(){
        initSemesterRowSet();
        if(this.start == null)
            try{
                semrowset.first();
                this.start = this.semrowset.getDate("Start_Date").toLocalDate();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            
        return this.start;
    }

    public LocalDate getEnd(){
        initSemesterRowSet();
        if(this.end == null)
            try{
                semrowset.first();
                this.end = this.semrowset.getDate("End_Date").toLocalDate();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        
        return this.end;
    }

    public Course getCourse(){
        initSemesterRowSet();
        if(this.course == null)
            try{
                semrowset.first();
                this.course = new Course(this.semrowset.getString("Course_Name"), this.con);
            }
            catch(SQLException  ex){
                ex.printStackTrace();
            }
        return this.course;
    }

}