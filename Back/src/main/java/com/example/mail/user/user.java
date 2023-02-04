package com.example.mail.user;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.example.mail.Service.GsonStrats.ExcludefromIn;
import com.example.mail.Service.GsonStrats.ExcludefromOut;
import com.example.mail.mail.message;
import com.example.mail.mail.messageGroup;
// import com.google.gson.annotations.Expose;
// import com.example.mail.mail.systemBuilder;
public class user {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    @ExcludefromOut
    @ExcludefromIn
    private ArrayList<contact> contacts;
    @ExcludefromOut
    private String password;
    @ExcludefromOut
    @ExcludefromIn
    private messageGroup 
    inbox,
    sent,
    draft,
    trash;
    @ExcludefromOut
    @ExcludefromIn
    private Hashtable<String,messageGroup> folders;
    
    public void setContacts(ArrayList<contact> contacts) {
        this.contacts = contacts;
    }
    public Hashtable<String,messageGroup> getFolders(){
        return this.folders;
    }
    public user clone(){
        user c = new user();
        c.setEmail(this.email);
        c.setFirstName(this.firstName);
        c.setGender(this.gender);
        c.setLastName(this.lastName);
        c.setPassword(this.password);
        return c;
    }
    public ArrayList<contact> cloneContacts(){
        ArrayList<contact>c=new ArrayList<contact>();
        for(contact x:contacts){
            c.add(x.clone());
        }
        return c;
    }
    public user(){
    }
    public user(String firstName, String lastName, String gender, String email, String password, messageGroup inbox,
    messageGroup sent, messageGroup draft, messageGroup trash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.inbox = inbox;
        this.sent = sent;
        this.draft = draft;
        this.trash = trash;
        folders=new Hashtable<>();
        contacts=new ArrayList<>();
    }
    public void makeNewMessageGroups(){
        this.inbox=new messageGroup();
        this.draft=new messageGroup();
        this.sent=new messageGroup();
        this.trash=new messageGroup();
        this.contacts=new ArrayList<contact>();
        this.folders=new Hashtable<String, messageGroup>();
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public messageGroup getGroup(String where){
        switch(where){
            case "inbox":
            return this.inbox;
            case "sent":
            return this.sent;
            case "draft":
            return this.draft;
            case "trash":
            return this.trash;
            default:
            System.out.println(where);
            System.out.println("0000000000000000000000000000000");
            System.out.println(this.folders.keySet());
            if(this.folders.containsKey(where)){
                return this.folders.get(where);
            }
            return null;
        }
    }
    public void addTomessageGroup(ArrayList<message> from,messageGroup to){
        List<message> t=to.getMessages();
        for(message m:from){
            if(!t.contains(m)){
                t.add(m);
            }
        }
    }

    public void addMessage(String where,message m){
        this.getGroup(where).addMessage(m);
    }
    public void deleteMessage(String where,int index){
        this.getGroup(where).deleteMessage(index);
    }
    public void changeMessage(String where,int index,message m){
        this.getGroup(where).changeMessage(index, m);
    }
    public void cleanTrash(){
        for(int i=0;i<this.trash.size();i++){
            if(Period.between(LocalDateTime.parse(this.trash.getMessage(i).getDate()).toLocalDate(),LocalDate.now()).toTotalMonths()>=1){
                this.trash.deleteMessage(i);
            }
        }
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // public void setId(long id) {
    //     this.id = id;
    // }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // public long getId() {
    //     return id;
    // }

    public String getGender() {
        return gender;
    }
    public ArrayList<contact> getContacts(){
        return this.contacts;
    }
    public void addContact(contact e){
        this.contacts.add(e);
    }
    public void deleteContact(int index){
        this.contacts.remove(index);
    }
    public void deleteContact(String email){
        int index=-1;
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getEmail().equalsIgnoreCase(email)){
                index=i;
                break;
            }
        }
        if(index!=-1){
            contacts.remove(index);
            return;
        }
        throw new Error("contact was not found");    
    }
    public void renameContact(int index, String newName){
        if(index>=contacts.size()){
            return;
        }
        this.contacts.get(index).setName(newName);
    }
    public contact getContact(String email){
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getEmail().equalsIgnoreCase(email)){
                return contacts.get(i);
            }
        }
        return null;
    }
    public void renameContact(String email, String newName){
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getEmail().equalsIgnoreCase(email)){
                contacts.get(i).setName(newName);
                return;
            }
        }
        throw new Error("contact was not found");
    }
    public boolean IsContactWith(String x){
        System.out.println(x);
        // System.out.println(((contact)contacts.get(0)).getEmail());
        for (int i=0;i<contacts.size();i++) {
            System.out.println(contacts.get(i).getEmail());
            if(contacts.get(i).getEmail().equalsIgnoreCase(x)){
                return true;
            }
        }
        return false;
    }
    public void deleteFolder(String name){
        this.folders.remove(name);
    }
    public void renameFolder(String oldName,String newName){
        this.folders.put(newName, this.folders.remove(oldName));
    }
    public ArrayList<contact> sortContactsbyName(){
        ArrayList<contact> x=this.cloneContacts();
        Collections.sort(x, new SortbyName());
        return x;
    }
    public ArrayList<contact> sortContactsbyEmail(){
        ArrayList<contact> x=this.cloneContacts();
        Collections.sort(x, new SortbyEmail());
        return x;
    }
    public void setFolders(Hashtable<String, messageGroup> folder) {
         this.folders=folder;
    }
    public ArrayList<contact>search(String text){
        ArrayList<contact> x=new ArrayList<contact>();
        for (contact c : contacts) {
            if(c.getEmail().contains(text)||c.getName().contains(text)){
                x.add(c);
            }
        }
        return x;
    }
    public void update(user newUser){
        this.firstName=newUser.firstName;
        this.lastName=newUser.lastName;
        this.gender=newUser.gender;
        this.password=newUser.password;
        this.email=newUser.email;
    }
}

//helper classes for sortings
class SortbyName implements Comparator<contact>{
    @Override
    public int compare(contact o1, contact o2) {
        return (o1.getName().compareTo(o2.getName()));
    }
}
class SortbyEmail implements Comparator<contact>{
    @Override
    public int compare(contact o1, contact o2) {
        return (o1.getEmail().compareTo(o2.getEmail()));
    }
}