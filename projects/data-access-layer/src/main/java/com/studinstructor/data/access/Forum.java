package com.studinstructor.data.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;



public abstract class Forum implements Serializable{
    protected int forumId = 0;
    protected String forumName = null;
    protected ForumType  type = null;
    protected Set<User> forumMediator = null;
    protected Connection con= null;
    //describe the forum's ongoings
    protected String forumdesc = null;
    protected List<SubForum> subForums = null;
    protected ResultSet forumrowset = null;

    protected Forum(){};

    protected Forum(int forumId, Connection con) throws IllegalArgumentException{
        this();
        if(forumId == 0) throw new IllegalArgumentException();
        if(con == null) throw new IllegalArgumentException();
        this.forumId = forumId;
        this.con = con;
    }

    protected Forum(String forumName, Connection con) throws IllegalArgumentException{
        this();
        if(forumName == null || forumName == "") throw new IllegalArgumentException();
        if(con == null) throw new IllegalArgumentException();
        this.forumName = forumName;
        this.con = con;
    }

    protected void initForumRowSet() throws IllegalStateException{
        if(forumrowset== null){
            try{
                PreparedStatement stmt;
                if(this.forumId!=0 && con != null){
                    stmt = con.prepareStatement("select * from v_forum where Forum_Id" + this.forumName, 
                                                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                }
                else{
                    throw new IllegalArgumentException();
                }
                if(stmt != null)
                    this.forumrowset = stmt.executeQuery();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        } 
    }

    public List<Forum> subForums(){
        List<Forum> names = new ArrayList<Forum>();
        try{
            PreparedStatement stmt = con.prepareStatement("select * from v_subforum where Forum_Id="+ this.forumId, 
                                                            ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while(rs.next()){
                names.add(new SubForum(rs.getInt("Subfor_Id"), this.con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return names;    
    }

    public Set<User> getForumMediator(){
        if(this.forumMediator == null){
            Set<User> users = new HashSet<User>();
            try{    
                PreparedStatement stmt = con.prepareStatement("select * from v_forum_mediator where SubFor_Id="+ this.forumId, 
                                                                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet rs = stmt.executeQuery();
                rs.beforeFirst();
                while(rs.next()){
                    users.add(new Student(rs.getInt("User_Id"), this.con));
                }
                this.forumMediator = users;
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.forumMediator;   
    }
 
    public String getForumName(){
        if(this.forumName == null){
            initForumRowSet();
        }
        try{
            forumrowset.first();
            this.forumName =forumrowset.getString("Forum_name");
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return this.forumName;
    }

    public List<String> getAssocFields(){   
        // get a list of enumerated objects representing topics covered by this forum
        return Collections.emptyList();
    }

    public void refresh(){
        this.forumMediator = null;
        this.forumrowset = null;
    }
}

