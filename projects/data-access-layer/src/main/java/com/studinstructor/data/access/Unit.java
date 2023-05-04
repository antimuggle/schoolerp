package com.studinstructor.data.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Unit implements Serializable {
    private String  uname = null;
    private String unitId = null;
    private  Department dept = null;
    private Course course = null;
    private Connection con = null;

    public Unit(String unitId, Connection con){
        
        this.unitId = unitId;
        this.con = con;
        initUnit();   

    }

    private void initUnit(){
        try{
            con.setAutoCommit(false);
            PreparedStatement stmt;
            if(this.unitId != null && this.con != null){
                //unitId is actually a string
                stmt= con.prepareStatement("SELECT Sport_Name, Username FROM v_unitinfo WHERE Unit_Id=" + this.unitId,
                                                            ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            }
            else{
                stmt = con.prepareStatement("SELECT Sport_Name, Username FROM v_unitinfo WHERE Unit_Name=" + this.uname,
                                                             ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            }
        
            ResultSet set = stmt.executeQuery();
            set.first();
        
            this.uname = set.getString("Unit_Name");
            this.unitId = set.getString("Unit_Id");
            this.dept = new Department(set.getString("Dept_Name"), this.con);
            this.course = new Course(set.getString("Course_Name"), this.con);

        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public String getName(){

        return this.uname;        
    }

    public String getUnitId(){

        return this.unitId;
    }

    public Department getDept(){

        return this.dept;
    }

    public Course getCourse(){

        return this.course;
    }


}
