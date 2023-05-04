package com.studinstructor.data.access;

import java.util.*;
import java.time.*;
import java.sql.*;


public final class Club extends Cocurricular{
    private int ClubId= 0;
    private String name = null;
    private LocalDate formed = null;
    private User custodian = null;
    private ResultSet clubrowset = null;
    private Connection con = null;
    private CocurrType type = CocurrType.CLUB;

    private Club(){};

    public Club(String name, Connection con) throws IllegalArgumentException{
        this();
        if(name == null || name == "") throw new IllegalArgumentException();
        if (this.con == null) throw new IllegalArgumentException();
        this.name = name;
        this.con = con;
    }

    public Club(int ClubId, Connection con) throws IllegalArgumentException{
        if(ClubId == 0) throw new IllegalArgumentException();
        if(con == null) throw new IllegalArgumentException();
        this.ClubId = ClubId;
        this.con = con;
    }

    public int getClubId(){
        return this.ClubId;
    }

    private void initClubRowSet(){
        try{
            PreparedStatement stmt = null;
            if(clubrowset == null){
                if(this.ClubId != 0 && this.con != null){ 
                    stmt = con.prepareStatement("select * from v_Club where Club_Id" + this.ClubId, ResultSet.CONCUR_READ_ONLY,
                                                                 ResultSet.CLOSE_CURSORS_AT_COMMIT );
                }
                else if(this.name != "" && this.name !="" && this.con != null) {
                    stmt = con.prepareStatement("select * from v_Club where Club_Name" + this.name, ResultSet.CONCUR_READ_ONLY,
                                                                     ResultSet.CLOSE_CURSORS_AT_COMMIT );
                }
                else{
                    throw new IllegalStateException();
                }
            }
            if(stmt != null){
                clubrowset = stmt.executeQuery();
            }
        } 
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    @Override
    public String getName(){
        initClubRowSet();
        try{
            clubrowset.first();
            this.name = clubrowset.getString("Club_Name");
            //if returns null then the value isnt in the database and should probably be an error
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return this.name; 
    }

    public LocalDate getformed(){
       
        initClubRowSet();
        try{
            clubrowset.first();
            this.formed =LocalDate.parse(clubrowset.getString("Date_formed"));
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
            PreparedStatement stmt = con.prepareStatement("SELECT Club_Name, Username FROM v_club_members WHERE Club_Id=" + this.ClubId,ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet set = stmt.executeQuery();
            set.first();
            while(set.next()){
                String username = set.getString("Username");
                if (username== "" || username == null){
                //error message inconsistent state in database
                }
                studentset.add(new Student(username, this.con));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return studentset;
    }

    @Override
    public CocurrType getType(){
        return this.type;
    }

    @Override
    public User getCustodian(){
        initClubRowSet();
        if(this.custodian == null)
            try{
                clubrowset.first();
                //check if moderator is a student or a lecturer
                Character custodiantype = this.clubrowset.getString("Creator_type").charAt(0);
                if(Character.toLowerCase(custodiantype) == 's')
                    this.custodian = (User) new Student(clubrowset.getInt("Creator_Id"), this.con);
                else if(Character.toLowerCase(custodiantype) == 'I')    
                    this.custodian = (User) new Instructor(clubrowset.getInt("Creator_Id"),this.con);
                else
                    this.custodian = (User) new Admin(clubrowset.getInt("Creator_Id"), this.con);
                    //check if moderator is a student or a lecturer
                }
            catch(SQLException ex){
                ex.printStackTrace();
            }    
        return this.custodian;
    }

    public void refresh(){
        this.clubrowset = null;
        this.formed =null;
        this.custodian = null;
    }

}
