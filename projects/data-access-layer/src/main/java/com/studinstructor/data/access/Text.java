package com.studinstructor.data.access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Text {
    private int textId = 0;
    private String text = null;
    private Connection con = null;
    private ResultSet rs = null;

    public Text(int textId, Connection con){
        this.textId = textId;
        this.con = con;
    }
    
    private void initResultSet(){
        if(this.con != null && this.rs == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select * from v_image where text_Id =" + this.textId,
                                                            ResultSet.CLOSE_CURSORS_AT_COMMIT,ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                rs = stmt.executeQuery();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public int getTextId(){
            return this.textId;
    }

    public String getSneekPeek(){
        String peek = this.getText().substring(0, 32);
        return peek;
    }

    public String getText(){
        initResultSet();
        if(rs!= null){
            try{
                this.text = rs.getString("text");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return this.text; 
        
    }
}