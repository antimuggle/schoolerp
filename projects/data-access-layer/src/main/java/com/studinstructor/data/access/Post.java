package com.studinstructor.data.access;

import java.time.*;
import java.util.*;
import java.io.Serializable;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post implements Serializable{
    private int postId = 0;
    private Room room = null;
    private User Creator = null;
    private LocalDate posted = null;
    private Map<String, String> files = null;
    private Text text = null;
    private Image image = null;
    private ResultSet postresultset = null;
    protected Connection con = null;

    public Post(int postId, Connection con){
        this.con = con;
        this.postId = postId;
    }

    public int getPostId(){

        return this.postId;
    }

    private void initResultSet(){
        try{
            con.setAutoCommit(false);
            PreparedStatement stmt = null;
            if(postresultset == null){
                if(this.postId != 0 && this.con != null)
                    stmt = con.prepareStatement("select * from v_Post where Post_Id" + this.postId, ResultSet.CONCUR_READ_ONLY,
                                                                 ResultSet.CLOSE_CURSORS_AT_COMMIT);
                else{
                    throw new IllegalStateException();
                }
            }
            if(stmt != null){
                postresultset = stmt.executeQuery();
            }
        }
        catch(SQLException ex){
                ex.printStackTrace();
        }                 
    }

    public LocalDate getPostDate(){
        initResultSet();
        try{
            if(postresultset!= null){
                this.posted = postresultset.getDate("Posted").toLocalDate();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        if(this.posted != null)
        
        return this.posted;
        else throw new IllegalStateException();
    }

    public Room getRoom(){
        initResultSet();
        try{
            if(postresultset != null)
            {
                PreparedStatement stmt = this.con.prepareStatement("select count(Child_Id) from v_roomsubchild where Child_Id=",
                                                                            +postresultset.getInt("Room_Id"), ResultSet.CONCUR_READ_ONLY, 
                                                                                                    ResultSet.TYPE_SCROLL_SENSITIVE);
                
                ResultSet set = stmt.executeQuery();
                set.first();
                if(set.getInt("Room_Id") == 0){
                    this.room =(Room) new MainRoom(this.postresultset.getInt("Room_Id"), this.con);
                }
                else
                    this.room =(Room) new SubRoom(this.postresultset.getInt("Room_Id"), this.con);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        return this.room;

    }
    
    public User getCreator(){
        initResultSet();
        try{
            if(postresultset != null)
            {   
                //possibly use characterstream?
                Character creatortype = this.postresultset.getString("Creator_Type").charAt(0);
                if(Character.toLowerCase(creatortype) == 's')
                    this.Creator = (User) new Student(postresultset.getInt("Creator_Id"), this.con);
                else if(Character.toLowerCase(creatortype) == 'I')    
                    this.Creator = (User) new Instructor(postresultset.getInt("Creator_Id"),this.con);
                else
                    this.Creator = (User) new Admin(postresultset.getInt("Creator_Id"), this.con);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return this.Creator;
    }

    public Map<String, String> getFilePaths(){
        if(this.files == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select * from v_Postfiles where Post_Id=" + this.postId, ResultSet.CLOSE_CURSORS_AT_COMMIT,
                                                                        ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs = stmt.executeQuery();
                rs.beforeFirst();
                while(rs.next()){
                    this.files = new HashMap<>();
                    this.files.put(rs.getString("File_Dec"), rs.getString("File_Location"));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }

        return this.files;
    }

    public Text getText(){
        // clob functionality
        if(this.text == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select * from v_Posttext where Post_Id=" + this.postId, ResultSet.CLOSE_CURSORS_AT_COMMIT,
                                                                        ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs = stmt.executeQuery();
                rs.beforeFirst();
                while(rs.next()){
                    this.text = new Text(rs.getInt("Text_Id"), this.con);
                }
            }
            catch(SQLException se){
                se.printStackTrace();
            }
        }
        return this.text;
    
    
    }
}
