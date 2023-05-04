package com.studinstructor.data.access;

import java.util.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public abstract class Room implements Serializable{
    private int roomId = 0; 
    private Instructor owner = null;
    private Unit unit = null;
    private List<Student> students = null;
    private List<Student> mediators = null;
    protected LocalDate formed = null;
    protected LocalDate expires = null;
    private List<Post> posts = null;
    private Semester semester = null;
    protected Connection con = null;
    private ResultSet roomresultset = null;

    protected Room(int roomId, Connection con){
        this.roomId = roomId;
        this.con = con;

    }

    private void initRoomResultSet(){
        if(this.roomresultset == null){
            try{
                PreparedStatement stmt = con.prepareStatement("select * from v_seminstructorinfo where Room_Id =" + this.roomId, 
                                                                ResultSet.CLOSE_CURSORS_AT_COMMIT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                roomresultset = stmt.executeQuery();
                if(this.semester== null){
                    PreparedStatement semstmt = con.prepareStatement("select Sem_Id from v_room where Room_Id=" + this.roomId,
                                                                    ResultSet.CLOSE_CURSORS_AT_COMMIT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = semstmt.executeQuery();
                    rs.first();
                    this.semester = new Semester(rs.getInt("Sem_Id"), this.con);
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public int getRoomId() throws IllegalStateException{
        if(this.roomId ==0){
            throw new IllegalStateException();
        }
        else 
            return this.roomId;
    }

    public Instructor getOwner(){
        if(this.con != null && this.owner == null){
            initRoomResultSet();
            try{
            roomresultset.first();
            this.owner = new Instructor(roomresultset.getInt("Inst_Id"), this.con);
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }                
        }
        return this.owner;
    }

    public  Unit getUnit(){
        if(this.con != null && this.unit == null){
            initRoomResultSet();
            try{
            roomresultset.first();
            this.unit = new Unit(roomresultset.getString("Unit_Id,"), this.con);
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.unit;
    }
    public List<Student> getMembers(){
        if(this.students == null && this.con != null){
            try{
                    PreparedStatement stmt = this.con.prepareStatement("select Stud_Id from v_studentrooms where Room_Id="
                                                                           +this.getRoomId() +"and Sem_Id="+this.semester.getSemId(),
                                                                            ResultSet.CONCUR_READ_ONLY,
                                                                            ResultSet.TYPE_SCROLL_SENSITIVE);
                    ResultSet set = stmt.executeQuery();
                    set.beforeFirst();
                    this.students = new ArrayList<Student>();
                    while(set.next()){
                        this.students.add(new Student(set.getInt("Stud_Id"), this.con));
                    }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return Collections.unmodifiableList(this.students);
    }
 
    public List<Student> getModeratorList(){
        try{
            PreparedStatement stmt = con.prepareStatement("select * from v_roommoderator where Room_Id =" + this.roomId, 
                                                                ResultSet.CLOSE_CURSORS_AT_COMMIT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            this.mediators = new ArrayList<>();
            rs.beforeFirst();
            while(rs.next()){
                this.mediators.add(new Student(rs.getInt("Stud_Id,"),this.con));
            }
        }
        catch(SQLException ex){
                ex.printStackTrace();
        }
        return Collections.unmodifiableList(this.mediators);        
    }

    public  abstract LocalDate created();

    public abstract LocalDate expires();

    public Semester getSemester(){
        if(this.con!= null && this.semester == null){
            initRoomResultSet();
        }
        return this.semester;
    }

// move to student and Instructor ejbs
    public List<Post> getPosts(){
        try{
            PreparedStatement stmt = con.prepareStatement("select * from v_roomposts where Room_No =" + this.roomId + 
                                                                ResultSet.CLOSE_CURSORS_AT_COMMIT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            this.posts = new ArrayList<Post>();
            rs.beforeFirst();
            while(rs.next()){
                this.posts.add(new Post(rs.getInt("Post_Id,"), this.con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return Collections.unmodifiableList(this.posts);
    }
}
