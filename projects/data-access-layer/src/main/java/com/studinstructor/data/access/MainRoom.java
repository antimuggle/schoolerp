package com.studinstructor.data.access;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class MainRoom extends Room{
    private ResultSet resultset = null;

    public MainRoom(int roomId, Connection connection){
        super(roomId, connection);
    }

    private void initResultSet(){
        if(this.con != null && this.resultset == null){
            try{
                this.resultset = this.con.createStatement().executeQuery("select * from v_semesterunitinfo where Sem_Id=" 
                                                                + getSemester().getSemId());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public LocalDate created(){
        if(this.formed == null){
            initResultSet();
            try{
            this.formed = this.resultset.getDate("Created").toLocalDate(); 
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
            return this.formed;
        
    }

    @Override
    public LocalDate expires(){
        if(this.expires == null){
            initResultSet();
            try{
            this.expires = this.resultset.getDate("Expires").toLocalDate(); 
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return this.formed;
    }
}
