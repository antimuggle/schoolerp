package com.studinstructor.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReplyPost extends Post{
    
    public Post parent; 

    public ReplyPost(int postId, Connection con){
        super(postId, con);
    }
    public Post getParent(){

        if(this.parent == null){
            try{
            PreparedStatement stmt = this.con.prepareStatement("select Parent_Id from v_postreplys where Post_Id="+ getPostId(), ResultSet.CLOSE_CURSORS_AT_COMMIT,
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.first();
            if(hasParent(rs.getInt("Parent_Id")))
                this.parent = new ReplyPost(rs.getInt("Parent_Id"), this.con);
            else this.parent = new Post(rs.getInt("Parent_Id"), this.con);
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.parent;
    }

    private boolean hasParent(int postId){
        try{        
            PreparedStatement stmt = this.con.prepareStatement("select count(*) from v_postreplys where Post_Id=" + postId,
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