package com.example.mail.user;

public class contact {
    String email;
    String name;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public contact clone(){
        contact c=new contact();
        c.email=this.email;
        c.name=this.name;
        return c;
    }
}
