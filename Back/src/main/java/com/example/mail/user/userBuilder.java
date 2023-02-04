package com.example.mail.user;

public class userBuilder {
    static public void buildNewUser(user x){
        if(x.getEmail()==null||x.getEmail().isEmpty()||
        x.getFirstName()==null||x.getFirstName().isEmpty()||
        x.getLastName()==null||x.getLastName().isEmpty()||
        x.getPassword()==null||x.getPassword().isEmpty()){
            throw new Error("Missing user info");
        }
        x.makeNewMessageGroups();
    } 
}
