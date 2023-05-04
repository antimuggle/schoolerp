package com.studinstructor.data.access;

import java.io.Serializable;

public abstract class  User implements Serializable{

   
    protected volatile byte[] profile= null;
    protected UserType type;
    
    protected User(){}

    protected User(UserType type){
    
    }

    public byte[] getProfile(){

        //fill logic to get profile pic
        return null;
    }
    
    public UserType getUserType(){
        
        return this.type;
    }

//to be used by threads and posts
}
 