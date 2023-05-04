package com.studinstructor.data.access;

import java.io.Serializable;

public final class Email implements Serializable{
    private String email;

    private Email(){};

    public Email(String email) {
        this();
        //check if email is valid
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

