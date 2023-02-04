package com.example.mail.user;

import java.util.ArrayList;

public class ContactsSorter {
    public static ArrayList<contact> SortGroup(user x,String by){
        if(by.equalsIgnoreCase("name")){
            return x.sortContactsbyName();
        }
        else if(by.equalsIgnoreCase("email")){
            return x.sortContactsbyEmail();
        }
        throw new Error("not provided sorting: "+by);
    }

}
