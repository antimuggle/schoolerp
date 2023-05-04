package com.studinstructor.data.access;

import java.io.Serializable;

public final class Contact implements Serializable{
    private int contacts;

    public Contact(int contacts) throws IllegalArgumentException{
        //check if the instace passed in is an int
        if(contacts == 0) throw new IllegalArgumentException();
        this.contacts = contacts;
    }

    public int getContacts() {
        return contacts;
    }
    
}
