package com.studinstructor.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;     

//should be an ejb
public class StudentRoomModerator extends Student implements RoomModerator{
    private List<Room> rooms = null;

    public StudentRoomModerator(Student stud, Room room) throws IllegalStateException{
        //check if student exists as a moderator and handle access
        super(stud.getStudId(), stud.con);
        try{
            if(!isRoomModerator(stud)){
                throw new IllegalStateException();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private StudentRoomModerator(Student newmoderator, Room room, Instructor inst) throws IllegalStateException{
        super(newmoderator.getStudId(),newmoderator.con);
        //add to room moderators this student and room
        //execute procedure currently in database
    }

    protected static synchronized RoomModerator createRoomModerator(Student assignee, Room room, Instructor inst){
        //create database table and log moderator creators
        //make sure student exists in the room
        //make sure only the instructor can make a request to this method
        StudentRoomModerator newnod = new  StudentRoomModerator(assignee, room, inst);
        return newnod;
    }
    
    @Override
    public List<Room> getRooms(boolean refresh){
        if(this.rooms == null && this.con != null || refresh && this.con != null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Room_Id from v_mainroommoderators where Stud_Id="+ this.getStudId(),
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet rs = stmt.executeQuery();
                rs.beforeFirst();
                this.rooms = new ArrayList<Room>();
                while(rs.next()){
                    this.rooms.add(new MainRoom(rs.getInt("Room_Id"), this.con));
                }
                stmt = this.con.prepareStatement("select Room_Id from v_childroommoderators where Stud_Id=" + this.getStudId(),
                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                rs = stmt.executeQuery();
                rs.beforeFirst();
                while(rs.next()){
                    this.rooms.add(new SubRoom(rs.getInt("Room_Id"), con));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.rooms;
    }

    private boolean isRoomModerator(Student stud) throws SQLException{
        PreparedStatement stmt = stud.con.prepareStatement("select count(*) from v_roommoderators where  Stud_Id="+ stud.getStudId(),
                                                                ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
        ResultSet rs = stmt.executeQuery();
        rs.first();
        if(rs.getInt(1) <=0)
            return false;
        else
            return true;
    }

    public boolean submitAssignment(Path file, Room room){
    }; 
}