package com.example.mail.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class messageGroup {
    ArrayList<message> messages;
    // SortbyDate DateSorter;
    public messageGroup clone(){
        messageGroup c=new messageGroup();
        for (message m:this.messages) {
            c.addMessage(m.clone());
        }
        return c;
    }
    public messageGroup(){
        this.messages=new ArrayList<message>();
        // this.DateSorter=new SortbyDate();
    } 
    public messageGroup(List<message> group){
        this.messages=(ArrayList<message>) group;
        // this.DateSorter=new SortbyDate();
    } 

    public int size(){
        return this.messages.size();
    }
    public message getMessage(int i){
        return messages.get(i);
    }
    public message getMessage(String id){
        for (int j = 0; j < messages.size(); j++) {
            if(messages.get(j).id.equals(id)){
                return messages.get(j);
            }
        }
        throw new Error("message id deosn't exist");
    }
    public void addMessage(message m){
        messages.add(m);
    }
    public void deleteMessage(int index){
        messages.remove(index);
    }
    public void deleteMessage(String id){
        for (int j = 0; j < messages.size(); j++) {
            if(messages.get(j).id.equals(id)){
                messages.remove(j);
                return;
            }
        }
        throw new Error("message id deosn't exist");
    }
    public void changeMessage(int index,message m){
        messages.set(index, m);
    }
    public List<message> getMessages(){
        return this.messages;
    }

    //--------------------------------------Searching--------------------------------------
    public messageGroup search(String text){
        messageGroup c=new messageGroup();
        for (message m : this.messages) {
            if(m.getBody().contains(text)||m.getSubject().contains(text)||m.getFrom().contains(text)||m.getTo().contains(text)||m.getAttachments().contains(text)){
                c.addMessage(m);
            }
        }
        return c;
    }
    //---------------------------------------Sorting---------------------------------------
    public messageGroup SortbyDate(){
        messageGroup c=this.clone();
        Collections.sort(c.messages,new SortbyDate());
        return c;
    }
    public messageGroup SortbySender(){
        messageGroup c=this.clone();
        Collections.sort(c.messages,new SortbyFrom());
        return c;
    }
    public messageGroup SortbyReceiver(){
        messageGroup c=this.clone();
        Collections.sort(c.messages,new SortbyTo());
        return c;
    }
    public messageGroup SortbyImportance(){
        messageGroup c= new messageGroup();
        PriorityQueue<message> q = new PriorityQueue<message>(new SortbyImportance());
        q.addAll(this.getMessages());
        // Collections.sort(c.messages,new SortbyImportance());
        // c.messages=q.toArray();
        for(int i=0;i<q.size();i++){
            c.addMessage(q.remove());
        }
        return c;
    }
    public messageGroup SortbyBody(){
        messageGroup c=this.clone();
        Collections.sort(c.messages,new SortbyBody());
        return c;
    }
    public messageGroup SortbySubject(){
        messageGroup c=this.clone();
        Collections.sort(c.messages,new SortbySubject());
        return c;
    }
    public messageGroup SortbyAttachments(){
        messageGroup c=this.clone();
        Collections.sort(c.messages,new SortbyAttachments());
        return c;
    }
    
}

class SortbyDate implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        
        return (o1.getDate().compareTo(o2.getDate()));
    }
}
class SortbyFrom implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        return (o1.getFrom().compareTo(o2.getFrom()));
    }
}
class SortbyTo implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        return (o1.getTo().compareTo(o2.getTo()));
    }
}
class SortbyBody implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        return (o1.getBody().compareTo(o2.getBody()));
    }
}
class SortbySubject implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        return (o1.getBody().compareTo(o2.getBody()));
    }
}
class SortbyImportance implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        return (Integer.valueOf(Integer.parseInt(o1.getImportance())).compareTo(Integer.parseInt(o1.getImportance())));
    }
}
class SortbyAttachments implements Comparator<message>{
    @Override
    public int compare(message o1, message o2) {
        return (Integer.valueOf(o1.getAttachments().size()).compareTo(o2.getAttachments().size()));
    }
}

