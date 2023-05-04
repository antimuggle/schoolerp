package com.studinstructor.data.access;

import java.util.*;


import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public final class Activity extends Cocurricular implements Serializable{
    private int actId = 0;
    private String actName = null;
    private LocalDate formed = null;
    private ResultSet activityrowset = null;
    private Connection con= null;
    private LocalDate endDate= null;
    private User custodian = null;
    private CocurrType type = CocurrType.ACTIVITY;
    
    private Activity(){};
    
    public Activity(int actId, Connection con)throws IllegalArgumentException{
        this();
        if(actId == 0) throw new IllegalArgumentException();
        if(this.con == null) throw new IllegalArgumentException();
        this.actId = actId;
        this.con = con;
    }
    
    public Activity(String actName, Connection con)throws IllegalArgumentException{        
        if (actName =="" || actName == null) throw new IllegalArgumentException();
        if (con == null) throw new IllegalArgumentException();
        this.actName = actName;
        this.con = con;
    }

    private void initActivityRowSet() throws IllegalStateException{
        try{
            PreparedStatement stmt = null;
            if(activityrowset == null){
                if(this.actId != 0 && this.con != null){ 
                    stmt = con.prepareStatement("select * from v_Activity where Activity_Id" + this.actId, ResultSet.CONCUR_READ_ONLY,
                                                                     ResultSet.CLOSE_CURSORS_AT_COMMIT);
                }
                else if(actName != "" && this.actName !="" && this.con != null) {
                    stmt = con.prepareStatement("select * from v_Activity where Activity_Name" + this.actName, ResultSet.CONCUR_READ_ONLY,
                                                                     ResultSet.CLOSE_CURSORS_AT_COMMIT);
                }
                else{
                    throw new IllegalStateException();
                }
            }
            if(stmt != null){
                activityrowset = stmt.executeQuery();
            }
            else throw new IllegalStateException();
        }
        catch(SQLException ex){
            //loggers to print exceptions for maintenance purposes
            ex.printStackTrace();
        }      
    }

    public int  getId(){
        return this.actId;
    }

    public String getName(){
        initActivityRowSet();
        try{
            activityrowset.first();
            this.actName = activityrowset.getString("Activity_Name");
            //if returns null then the value isnt in the database and should probably be an error
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return this.actName;
    }

    public LocalDate getFormed(){
        initActivityRowSet();
        try{
        activityrowset.first();
        //convert to Date then to localdate
        this.formed = activityrowset.getDate("Date_Formed").toLocalDate();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return this.formed;
    }
    
    @Override
    public Set<User> getMembers(){
        HashSet<User> studentset= new HashSet<User>();
        try{    
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("SELECT Activity_Name, Username FROM v_activity_members WHERE Activity_Id=" + this.actId, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet set = stmt.executeQuery();
            set.beforeFirst();
            while(set.next()){
                String username = set.getString("username");
                if (username== "" || username == null)
                //error message inconsistent state in database
                studentset.add((Student)new Student(username, this.con));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return studentset;
    }

    @Override
    public User getCustodian(){
        initActivityRowSet();
        if(this.custodian == null)
            try{
                activityrowset.first();
                //check if moderator is a student or a lecturer
                Character custodiantype = this.activityrowset.getString("Creator_type").charAt(0);
                if(Character.toLowerCase(custodiantype) == 's')
                    this.custodian = (User) new Student(activityrowset.getInt("Creator_Id"), this.con);
                else if(Character.toLowerCase(custodiantype) == 'I')    
                    this.custodian = (User) new Instructor(activityrowset.getInt("Creator_Id"),this.con);
                else
                    this.custodian = (User) new Admin(activityrowset.getInt("Creator_Id"), this.con);
                //check if moderator is a student or a lecturer
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        return this.custodian;
    }
    
    @Override
    public CocurrType getType() {    
        return this.type;
    }

    public LocalDate getEndDate(){
        initActivityRowSet();
        if(this.endDate == null)
            try{
                activityrowset.first();
                this.endDate =  activityrowset.getDate("endDate").toLocalDate();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        return this.endDate;
    }

    public void refresh(){
        this.activityrowset = null;
        this.formed=null;
        this.endDate = null;
    }
}
