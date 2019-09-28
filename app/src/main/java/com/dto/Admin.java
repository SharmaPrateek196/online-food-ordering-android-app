package com.dto;

import java.io.Serializable;

public class Admin implements Serializable
{
    private String id;
    private String password;

    public Admin() {
    }

    public Admin(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
