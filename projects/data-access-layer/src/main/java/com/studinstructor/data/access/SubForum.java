package com.studinstructor.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubForum extends Forum{

    private Forum parent = null;
    
    public SubForum(String name, Connection con){
        super(name,con);
        initParentForum();
    }

    public SubForum(int forId, Connection con){
        super(forId, con);
        initParentForum();
    }

    private void initParentForum(){
        try{
            PreparedStatement stmt = this.con.prepareStatement("select * from v_subforum where SubFor_Name"+ this.forumId, 
                                                            ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet rs = stmt.executeQuery();
            rs.first();
            this.parent = new MainForum(rs.getInt("Subfor_Id"), this.con);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public Forum getParentForum(){   
        return this.parent;
    }
}
