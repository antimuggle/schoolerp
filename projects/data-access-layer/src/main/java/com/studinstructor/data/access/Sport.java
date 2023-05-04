package com.studinstructor.data.access;

import java.util.*;
import java.sql.*;
import java.time.*;

public final class Sport extends Cocurricular{
    private int sportId = 0;
    private String sportName = null;
    private LocalDate formed = null;
    private ResultSet sportrowset = null;
    private Connection con= null;
    private User custodian = null;
    private CocurrType type = CocurrType.SPORT;
    
    private Sport(){};
    
    public Sport(int sportId, Connection con){
        
        this();
        this.sportId = sportId;
        this.con = con;
    }
    
    public Sport(String sportName, Connection con){
        this.sportName = sportName;
        this.con = con;
    }

    private void initSportRowSet(){
        try{
            PreparedStatement stmt = null;
            if(sportrowset == null){
                if(this.sportId != 0 && this.con != null){ 
                    stmt = con.prepareStatement("select * from SPORT where Sport_Id" + this.sportId, ResultSet.CONCUR_READ_ONLY,
                                                                 ResultSet.CLOSE_CURSORS_AT_COMMIT );
                }
                else if(sportName != "" && this.sportName !="" && this.con != null) {
                    stmt = con.prepareStatement("select * from SPORT where Sport_Name" + this.sportName, ResultSet.CONCUR_READ_ONLY,
                                                                     ResultSet.CLOSE_CURSORS_AT_COMMIT );
                }
                else{
                    throw new IllegalStateException();
                }
            }
            if(stmt != null){
                sportrowset = stmt.executeQuery();
            }
        }
        catch(SQLException ex){
                ex.printStackTrace();
        }           
    }

    public int  getId(){
        return this.sportId;
    }

    public String getName(){    
        initSportRowSet();
        try{
            sportrowset.first();
            this.sportName = sportrowset.getString("Sport_Name");
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        //if returns null then the value isnt in the database and should probably be an error
        return this.sportName;
    }

    public LocalDate getFormed(){
        
        initSportRowSet();
        try{
            sportrowset.first();
            this.formed =LocalDate.parse(sportrowset.getString("Date_Formed"));
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        return this.formed;
    }

    @Override
    public Set<User> getMembers(){
        
        Set<User> studentset= new HashSet<User>();
        try{
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("SELECT Sport_Name, Username FROM v_sport_members WHERE Sport_Id=" + this.sportId, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet set = stmt.executeQuery();
            set.first();
            while(set.next()){
                String username = set.getString("username");
                if (username== "" || username == null)
                //error message inconsistent state in database
                studentset.add( new Student(username, this.con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        return studentset;   
    }

    @Override
    public User getCustodian(){
        initSportRowSet();
        try{
            sportrowset.first();
            Character custodiantype = this.sportrowset.getString("Creator_type").charAt(0);
            if(Character.toLowerCase(custodiantype) == 's')
                this.custodian = (User) new Student(sportrowset.getInt("Creator_Id"), this.con);
            else if(Character.toLowerCase(custodiantype) == 'I')    
                this.custodian = (User) new Instructor(sportrowset.getInt("Creator_Id"),this.con);
            else
                this.custodian = (User) new Admin(sportrowset.getInt("Creator_Id"), this.con);
                //check if moderator is a student or a lecturer
                //this.currentCust = new Moderator(clubrowset.getInt("Current_Cust"));
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return this.custodian;
    }

    @Override
    public CocurrType getType(){
        return this.type;
    }

    public void refresh(){
        this.sportrowset = null;
        this.formed=null;
        this.custodian=null;
    }
}