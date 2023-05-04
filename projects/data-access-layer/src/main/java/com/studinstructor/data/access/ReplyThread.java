package com.studinstructor.data.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class ReplyThread extends Thread{
    
    public Thread parent = null; 
    public ReplyThread(int threadId, Connection con){
        super(threadId, con);
    }


    public Thread getParent(){
        if(this.parent == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Thread_Id from v_threadreplys where Reply_Id="+ this.getThreadId(),
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs = stmt.executeQuery();
                rs.first();
                
                if(hasParent(rs.getInt("Thread_Id")))
                    this.parent = new ProblemThread(rs.getInt("Thread_Id"), this.con);
                else 
                {
                    this.parent = new ReplyThread(rs.getInt("Thread_Id"), this.con);
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.parent;
        

    }
    private boolean hasParent(int threadId){
        try{    
            PreparedStatement stmt = this.con.prepareStatement("select count(*) from v_threadreplys where Reply_Id=" + threadId,
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
            ResultSet rs = stmt.executeQuery();
            rs.first();
            if(rs.getInt(1) > 0)
                return false;
            else return false;                                       
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
