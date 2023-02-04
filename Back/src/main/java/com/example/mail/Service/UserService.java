package com.example.mail.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.mail.mail.messageBuilder;
import com.example.mail.user.user;

import org.springframework.stereotype.Component;
// import org.springframework.stereotype.Service;

@Component
public class UserService {
    
    
    List<user> users;

    public UserService(){
        //load users
        users=new ArrayList<>();
        Save singelton =Save.getInstance();
        // try {
        //     singelton.save(users);
        //     System.out.println("HIIIIIIIIIIIIIiii");
        // } catch (IOException e1) {
        //     e1.printStackTrace();
        // }
        try {
            
            ArrayList<String> loadedIds=singelton.loadIds();    //implement loadIds && saveIds plz
            messageBuilder.loadIds(loadedIds);

            users= singelton.load();
            // System.out.println(users.size());
            if(users==null){
                System.out.println("no file was created yet");
                this.users=new ArrayList<user>();
                return;
            }
            // System.out.println("loaded successfully");
        }catch(Exception e){
            System.out.println(e);
        }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // }
    }
    public int Size(){
        return this.users.size();
    }
    public void addUser(user x){
        this.users.add(x);
    }
    public boolean isEmailAvail(String email){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getEmail().equalsIgnoreCase(email)){
                return false;
            }
        }
        return true;
    }
    public int getIndexByEmail(String email){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getEmail().equalsIgnoreCase(email)){
                return i;
            }
        }
        throw new Error("Email not found");
    }
    public user getUserByIndex(int index){
        if(index<this.users.size()){
            return this.users.get(index);
        }
        throw new Error("Out of bound user index");
    }
    public user getUserByEmail(String email){
        for (user x : users) {
            if(x.getEmail().equals(email)){
                return x;
            }
        }
        return null;
    }
    public user getUserByName(String name){
        for (user x : users) {
            if(x.getFirstName().equalsIgnoreCase(name)||x.getLastName().equalsIgnoreCase(name)){
                return x;
            }
        }
        return null;
    }
    public void update(){
        try {
            Save singelton =Save.getInstance();
            singelton.save(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void deleteUser(int index) {
        this.users.remove(index);
    }

    




}
