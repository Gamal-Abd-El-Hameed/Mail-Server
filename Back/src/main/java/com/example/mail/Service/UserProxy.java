package com.example.mail.Service;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import com.example.mail.user.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProxy {
    
    UserService uService;
    Hashtable<UUID, Integer> sessions;
    
    @Autowired
    public UserProxy(){
        this.uService=new UserService();
        this.sessions=new Hashtable<UUID,Integer>();
    }
    public boolean IDexists(UUID id){
        return (this.sessions.containsKey(id));
    }
    public Integer sessionExistsbyEmail(String email){
        for (int index : Collections.list(sessions.elements())){
            if(this.getUserbyIndex(index).getEmail().equalsIgnoreCase(email)){
                return index;
            }
        }
        return null;
    }
    public boolean emailExists(String email){
        return(!this.uService.isEmailAvail(email));
    }
    public void removeSession(UUID id){
        this.sessions.remove(id);
    }
    public user getUserById(UUID id){
        return(this.getUserbyIndex(this.getIndexById(id)));
    }
    public UUID getIdByIndex(int index){
        Integer value=index;
        UUID id;
        for(Map.Entry<UUID,Integer> entry: sessions.entrySet()){
            if(value.equals(entry.getValue())){
                id = entry.getKey();
                return id;
            }
        }
        return null;
    }

    public int getIndexById(UUID id){
        return(this.sessions.get(id));
    }
    public UUID addNewUser(user newUser){
        this.addUser(newUser);
        return this.addSession(this.uService.Size()-1);
    }
    public UUID addSession(int index){
        UUID id;
        do{
            id=UUID.randomUUID();
        }
        while(Collections.list(sessions.keys()).contains(id));
        sessions.put(id, index);
        return id;
    }
    public user getUserbyEmail_Validated(String email, String password){
        user x=this.uService.getUserByEmail(email);
        if(x==null){throw new Error("Email was not found");}
        if(x.getPassword().equals(password)){
            return x;
        }
        else{
            throw new Error("unmatching password");
        }
    }
    public user getUserbyEmail_notValidated(String email){
        user x=this.uService.getUserByEmail(email);
        if(x==null){throw new Error("Email was not found");}
        return x;
    }
    public user getUserbyName_notValidated(String name){
        user x=this.uService.getUserByName(name);
        if(x==null){throw new Error("Name was not found");}
        return x;
    }
    public void addUser(user x){
        this.uService.addUser(x);
    }
    public int getIndexByEmail(String email){
        return this.uService.getIndexByEmail(email);
    }
    public user getUserbyIndex(int index){
        return this.uService.getUserByIndex(index);
    }
    public void deleteUser(UUID id) {
        int index=this.getIndexById(id);
        this.removeSession(id);
        this.uService.deleteUser(index);
        for(Map.Entry<UUID,Integer> entry: sessions.entrySet()){
            if(index<(entry.getValue())){
                entry.setValue(entry.getValue()-1);
            }
        }
        return;
    }
}
