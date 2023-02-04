package com.example.mail.mail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.example.mail.Service.Save;

public class messageBuilder {
    static ArrayList<String> ids;
    public static void build(message m){
        m.dateTime=LocalDateTime.now().toString();
        do{
            m.id=UUID.randomUUID().toString();
        }
        while(ids.contains(m.id));
        ids.add(m.id);
        Save singleton=Save.getInstance();
        singleton.saveIds(ids);     
    }
    public static void loadIds(ArrayList<String> i){
        ids=i;
    }
    public static ArrayList<String> getIds(){
        return ids;
    }
}
