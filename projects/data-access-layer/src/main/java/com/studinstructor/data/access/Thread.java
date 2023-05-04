package com.studinstructor.data.access;

import java.time.*;
import java.util.*;
import java.io.Serializable;
import java.nio.*;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//files and text still need work
public abstract class Thread implements Serializable {
    private int threadId = 0;
    private User creator = null;
    private volatile int upVotes = 0;
    private volatile int downVotes = 0;
    private LocalDate posted = null;
    private List<Path> files = Collections.emptyList();
    private String text = null;
    private List<ReplyThread> replys = Collections.emptyList();
    protected Connection con = null;
    private ResultSet threadrowset =  null;

    public Thread(int threadId, Connection con){

        this.threadId = threadId;
        this.con = con;
    }

    private void initThreadRowSet(){
        if(this.con != null && this.threadrowset == null)
            try{
                PreparedStatement stmt = this.con.prepareStatement("select * from v_threadInfo where Thread_id= " + this.getThreadId(),
                                                                        ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                this.threadrowset = stmt.executeQuery();
            }
            catch(SQLException e){
                e.printStackTrace();
            }

    }

    public int getThreadId(){
        
        return this.threadId;
    }

    public User getCreator(){
        initThreadRowSet();
        if(this.creator==null){ 
            try{
                    this.threadrowset.first();
                    Character usertype = this.threadrowset.getString("Creator_type").charAt(0);
                    if(Character.toUpperCase(usertype) == 'S')
                        this.creator = (User) new Student(this.threadrowset.getInt("Creator"), this.con);
                    else if(Character.toUpperCase(usertype) == 'I') 
                        this.creator = new Instructor(this.threadrowset.getInt("Creator"), this.con);
                    else if(Character.toUpperCase(usertype) == 'A')
                        this.creator = new Admin(this.threadrowset.getInt("Creator"), this.con);
                }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return this.creator;
    }

    protected int getUpvotes(boolean refresh){
        if(refresh)
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Upvotes from v_threadInfo where threadId =" +this.getThreadId(),
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet set = stmt.executeQuery();
                set.first();
                this.upVotes = set.getInt("Upvotes");
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            return this.upVotes;
    }

    protected int getDownVotes(boolean refresh){
        
        if(refresh)
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Downvotes from v_threadInfo where threadId =" +this.getThreadId(),
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet set = stmt.executeQuery();
                set.first();
                this.downVotes = set.getInt("Downvotes");
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            return this.downVotes;
    }

    public LocalDate getPosted(){
            initThreadRowSet();
            try{
                if(threadrowset!= null){
                    this.posted = threadrowset.getDate("Posted").toLocalDate();
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            if(this.posted != null)   
            return this.posted;
            else throw new IllegalStateException();
    }

    public List<ReplyThread> getReplys(boolean refresh){
        if(refresh)
            try{    
                PreparedStatement stmt = this.con.prepareStatement("select Reply_Id from v_threadreplys where Thread_Id =" + getThreadId(),
                                                                        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet set = stmt.executeQuery();
                set.beforeFirst();
                this.replys = new ArrayList<>();
                while(set.next()){
                    this.replys.add(new ReplyThread(set.getInt("Reply_Id"), this.con));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
        }
        return this.replys;
    }
}


    //files and text 



