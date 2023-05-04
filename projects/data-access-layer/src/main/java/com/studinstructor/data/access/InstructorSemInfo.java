package com.studinstructor.data.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class InstructorSemInfo implements Serializable{
    private Instructor inst = null;
    private Semester sem = null;
    private List<Unit> unitsTaught = null;
    private Connection con = null;

    public InstructorSemInfo(Instructor inst,Semester sem, Connection con) throws IllegalArgumentException{
        if(inst == null || sem == null || con == null) throw new IllegalArgumentException();
        this.inst = inst;
        this.sem = sem;
        this.con = con;
    }

    private void initInstructorSemInfo(){
        if(this.unitsTaught == null)
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Unit_Id from v_semesterunitinstructor where Sem_Id="+ this.sem.getSemId() +
                                                                        "AND Inst_Id ="+ this.inst.getId(), ResultSet.CONCUR_READ_ONLY,
                                                                            ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs  = stmt.executeQuery();
                rs.beforeFirst();
                unitsTaught = new ArrayList<Unit>();	
                while(rs.next()){
                    //change unitid to return string instead of int
                    unitsTaught.add(new Unit(rs.getString("Unit_Id"), this.con));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
    }

    public User getInstructor(){
        return this.inst;
    }

    public Semester getSem(){
        return this.sem;
    }

    public List<Unit> getUnits(){
        if(unitsTaught != null) 
            return Collections.unmodifiableList(this.unitsTaught);
        else if(unitsTaught == null){
            initInstructorSemInfo();
             return Collections.unmodifiableList(this.unitsTaught);

        }
        else
            return Collections.emptyList();

    }

}
