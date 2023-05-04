package com.studinstructor.data.access;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class Image {
    private int imgId = 0;
    private String imgName = null;
    private ByteBuffer imgData = null;
    private Connection con = null;
    private ResultSet resultset = null;

    public Image(int imgId){
        if(imgId == 0) throw new IllegalArgumentException();
        this.imgId = imgId;
    }

    private void initResultSet(){
        if(this.con != null && this.resultset == null){
            //create view in databaset
            try{
            PreparedStatement stmt = this.con.prepareStatement("select * from v_imagedetails where img_Id=" + this.imgId
                                                                      ,ResultSet.CLOSE_CURSORS_AT_COMMIT, ResultSet.CONCUR_READ_ONLY,ResultSet.TYPE_SCROLL_SENSITIVE);
        
            this.resultset = stmt.executeQuery();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public int getImgId() {
        return imgId;
    }

    public ByteBuffer getImg(){
        //access file server
        return this.imgData;
    }
}
