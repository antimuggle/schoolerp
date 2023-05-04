package com.studinstructor.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

enum ThreadState{
    Closed, Open, Pending;

}
public class ProblemThread extends Thread{
    private ReplyThread solution = null;
    private ThreadState state = null;
    private String title = null;
    private ResultSet titleresultset = null;
    
    public ProblemThread(int threadId, Connection con){
        super(threadId,con);
    }

    public ReplyThread getSolution(boolean refresh){
        if(refresh || this.solution == null)
            try{ 
                PreparedStatement stmt = con.prepareStatement("select Solution from v_problemthread where Thread_Id="  + this.getThreadId(),
                                                                            ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs = stmt.executeQuery();
                rs.first();
                this.solution = new ReplyThread(rs.getInt("Solution"), this.con);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        return this.solution;
    }

    public ThreadState getThreadState(){

        return this.state;
    }

    public String getTitle(){
        if (this.title == null)
            try{
                if(this.titleresultset == null) 
                    {
                        PreparedStatement stmt = con.prepareStatement("select Title from v_threadinfo where Thread_Id ="  + this.getThreadId(),
                                                                            ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                        titleresultset = stmt.executeQuery();
                    }
                titleresultset.first();
                this.title = titleresultset.getString("Title");
            
            }
            catch(Exception e){
                e.printStackTrace();
            }

        return this.title;
    }
    
}

