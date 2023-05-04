package com.studinstructor.data.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;

public class MainForum extends Forum{

    public MainForum(int forId, Connection con){
        super(forId, con);
    }

    public List<SubForum> getForumList(){
        List<SubForum> subfors = new ArrayList<SubForum>();
        try{
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM unit_List", ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet set = stmt.executeQuery();
            set.first();
            while(set.next()){
                subfors.add(new SubForum(set.getInt("Unit_Id"), this.con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return subfors;
    }

}
