package com.studinstructor.data.access;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.*;

public final class Instructor extends User{
    private int id = 0;
    private String name = null;
    private List<Email> email  = null;
    private List<Contact> contacts = null; 
    private Connection con = null;

    public Instructor(int id,Connection con) throws IllegalArgumentException{
        super(UserType.INSTRUCTOR);
        if(id == 0) throw new IllegalArgumentException();
        this.id = id;
        this.con = con;
        //initialise database info
    }

    public List<Contact> getContacts(){
        if(this.email == null && this.con == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Contacts from v_instructorcontacts where Inst_Id" + this.id,
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet set = stmt.executeQuery();
                this.contacts = new ArrayList<Contact>();
                set.beforeFirst();
                while(set.next()){
                    this.contacts.add(new Contact(set.getInt("Contacts")));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        
        }
        return Collections.unmodifiableList(this.contacts);

    }

    public int getId() throws IllegalArgumentException{
        if(this.id != 0)
            return this.id;
        else
            throw new IllegalArgumentException();
    }

    public String getName(){
        if(this.name == null || this.name ==""){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select * from v_instructornames where Inst_Id=" + this.id,
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet set = stmt.executeQuery();
                set.first();
                StringBuffer namebuilder = new StringBuffer();
                namebuilder.append(set.getString("F_Name"));
                namebuilder.append(set.getString("S_Name"));
                namebuilder.append(set.getString("L_Name"));
                this.name = namebuilder.toString();
            }
            catch(SQLException ex){
                ex.printStackTrace();   
            }
        }
        return this.name;
    }

    public List<Email> getEmail(){
        if(this.email == null && this.con == null){
            try{
                PreparedStatement stmt = this.con.prepareStatement("select Email from v_instructoremails where Inst_Id" + this.id,
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE);
                ResultSet set = stmt.executeQuery();
                this.email = new ArrayList<Email>();
                set.beforeFirst();
                while(set.next()){
                    this.email.add(new Email(set.getString("Email")));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }  
        }
        return Collections.unmodifiableList(this.email);
    }
//ADD ROOM TYPES,

    public void getDocuments(){

    }

    public void getRoomInfo(){


    }

    
    public void geRoomsInClass(){

    }

    public void mergeRooms(){

    }

    //belongs in ejb
    public void sendMessage(){}

}