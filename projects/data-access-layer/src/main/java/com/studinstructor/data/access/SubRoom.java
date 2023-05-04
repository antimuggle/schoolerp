package com.studinstructor.data.access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Connection;

public class SubRoom extends Room{
    private ResultSet resultset = null;
    private LocalDate expires = null;
    private LocalDate created = null;

    public SubRoom(int roomId, Connection con) {
        super(roomId, con);
    }

    private void initResultSet(){
        if(this.con!= null && this.resultset == null){
            try{
                //view not commited in database
                this.resultset = this.con.createStatement().executeQuery("SELECT * FROM v_subrooms WHERE room_id = " + this.getRoomId());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public LocalDate expires(){
        if(this.expires == null){
            initResultSet();
            try{
                this.resultset.first();
                this.expires = this.resultset.getDate("expires").toLocalDate();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.expires; 
    }

    public LocalDate created(){
        if(this.created == null){
            initResultSet();
            try{
                this.resultset.first();
                this.created = this.resultset.getDate("created").toLocalDate();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.created;
    }
}
