package com.example.mail.mail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.mail.Service.GsonStrats.ExcludefromIn;

public class message {
    public String getImportance() {
        return String.valueOf(importance);
    }
    public void setImportance(int importance) {
        this.importance = importance;
    }
    //emails
    @ExcludefromIn
    String id; 
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcludefromIn
    String from; 
    String to; 

    int importance;

    String subject;
    String body;
    List<String> attach;
    //date
    @ExcludefromIn
    String dateTime;
    public message clone(){
        message c= new message(this.from,this.to,this.subject,this.body,LocalDateTime.parse(this.dateTime),this.importance,this.id);
        for(String a:this.attach){
            c.addAttachment(a);
        }
        return c;
    }
    public message(String from, String to, String subject, String body, LocalDateTime dateTime,int importance,String id){
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body =body;
        this.dateTime = LocalDateTime.of(dateTime.toLocalDate(), dateTime.toLocalTime()).toString();
        this.importance=importance;
        this.id=id;
        this.attach=new ArrayList<String>();
    }
    public message(String to, String subject, String body,int importance){
        this.to = to;
        this.subject = subject;
        this.body =body;
        this.importance=importance;
        this.attach=new ArrayList<String>();
    }
    public String getFrom() {
        return from;
    }
    public String getDate(){
        if(!(this.dateTime instanceof String)){
            this.dateTime.toString();
        }
        return this.dateTime;
    }
    public void setDate(String dateTime){
         this.dateTime=dateTime;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getAttachments() {
        return attach;
    }
    public String getAttachment(int i) {
        return attach.get(i);
    }
    public void setAttachments(List<String> attach) {
        this.attach = attach;
    }
    public void addAttachment(String x) {
        attach.add(x);
    }
    public void changeAttachment(int index, String attach) {
        this.attach.set(index, attach);
    }
    public void removeAttachment(int index) {
        this.attach.remove(index);
    }
}
