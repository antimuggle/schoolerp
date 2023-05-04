package com.studinstructor.data.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudentSemInfo implements Serializable{
    private Student stud = null;
    private Semester sem = null;
    private  List<Unit> currentUnits = Collections.emptyList(); 
    private Connection con = null;

    public StudentSemInfo(Student stud, Semester sem, Connection con){

        this.stud = stud;
        this.sem = sem;
        this.con = con;
    }

    public Student getStudent(){
        return this.stud;
    }
    private void initStudentSemInfo(){
        if(this.currentUnits == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Unit_Id from v_semesterunitstudent where Sem_Id="+ this.sem.getSemId() +
                                                                        "AND  Stud_Id=" + this.stud.getStudId(), ResultSet.CONCUR_READ_ONLY,
                                                                            ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs = stmt.executeQuery();
                this.currentUnits = new ArrayList<Unit>();
                rs.beforeFirst();
                while(rs.next()){
                    this.currentUnits.add(new Unit(rs.getString("Unit_Id"), con));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public Semester getSem(){
        return this.sem;
    }

    public List<Unit> getUnits(){
        if(this.currentUnits != null)
            return Collections.unmodifiableList(this.currentUnits);
        else if(this.currentUnits == null)
        {
            initStudentSemInfo();
            return Collections.unmodifiableList(this.currentUnits);
        }
        else
            return Collections.emptyList();
    }

}
