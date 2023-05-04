package com.studinstructor.data.access;

import java.util.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.*;

public final class Association extends Cocurricular{
    private int assId = 0;
    private String assName = null;
    private LocalDate formed = null;
    private User custodian = null;
    private ResultSet associationrowset = null;
    private Connection con= null;
    private CocurrType type = CocurrType.ASSOCIATION;
    
    private Association(){};
    
    public Association(int assId, Connection con) throws IllegalArgumentException{
        this();
        if(con == null) throw new IllegalArgumentException();
        if (assId == 0) throw new IllegalArgumentException();
        this.assId = assId;
        this.con = con;
    }
    
    public Association(String assName, Connection con) throws IllegalArgumentException{ 
        if(assName == null || assName == "") throw new IllegalArgumentException();
        if(con == null) throw new IllegalArgumentException();
        this.assName = assName;
        this.con = con;
    }

    private void initAssociationRowSet() throws IllegalStateException{
        try{
            PreparedStatement stmt = null;
            if(associationrowset == null){
                if(this.assId != 0 && this.con != null){ 
                    stmt = con.prepareStatement("select * from v_Association where Ass_Id" + this.assId, ResultSet.CONCUR_READ_ONLY,
                                                                 ResultSet.CLOSE_CURSORS_AT_COMMIT );
                }
                else if(assName != "" && this.assName !="" && this.con != null) {
                    stmt = con.prepareStatement("select * from v_Association where Ass_Name" + this.assName, ResultSet.CONCUR_READ_ONLY,
                                                                     ResultSet.CLOSE_CURSORS_AT_COMMIT );
                }
                else{
                    throw new IllegalStateException();
                }
            }
            if(stmt != null){
                associationrowset = stmt.executeQuery();
            }
        }    
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    

    public int  getId(){
        return this.assId;
    }

    @Override
    public String getName(){

        
        initAssociationRowSet();
        try{
            associationrowset.first();
            this.assName = associationrowset.getString("Ass_Name");
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        //if returns null then the value isnt in the database and should probably be an error
        return this.assName;
    }

    public LocalDate getFormed(){
        
        initAssociationRowSet();
        try{
            associationrowset.first();
            this.formed =LocalDate.parse(associationrowset.getString("Date_Formed"));
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
            PreparedStatement stmt = con.prepareStatement("SELECT Ass_Name, Username FROM v_association_members WHERE Ass_Id=" + this.assId,ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet set = stmt.executeQuery();
            set.first();
            while(set.next()){
                String username = set.getString("username");
                if (username== "" || username == null)
                //error message inconsistent state in database
                studentset.add((Student) new Student(username, this.con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return studentset;

        
    }
    
    @Override
    public User getCustodian(){
        initAssociationRowSet();
        try{
            associationrowset.first();
            //check if moderator is a student or a lecturer
            //this.currentCust = new Moderator(clubrowset.getInt("Current_Cust"));
            Character custodiantype = this.associationrowset.getString("Creator_type").charAt(0);
            if(Character.toLowerCase(custodiantype) == 's')
                this.custodian = (User) new Student(associationrowset.getInt("Creator_Id"), this.con);
            else if(Character.toLowerCase(custodiantype) == 'I')    
                this.custodian = (User) new Instructor(associationrowset.getInt("Creator_Id"),this.con);
            else
                this.custodian = (User) new Admin(associationrowset.getInt("Creator_Id"), this.con);
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
        this.associationrowset = null;
    }
}
