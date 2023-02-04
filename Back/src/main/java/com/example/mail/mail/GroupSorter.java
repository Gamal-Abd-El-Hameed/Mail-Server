package com.example.mail.mail;

public class GroupSorter {
    public static messageGroup SortGroup(messageGroup group,String by){
         if(by.equalsIgnoreCase("sender")){
            return group.SortbySender();
        }
        else if(by.equalsIgnoreCase("receiever")){
            return group.SortbyReceiver();
        }
        else if(by.equalsIgnoreCase("importance")){
            return group.SortbyImportance();
        }
        else if(by.equalsIgnoreCase("subject")){
            return  group.SortbySubject();
        }
        else if(by.equalsIgnoreCase("body")){
            return group.SortbyBody();
        }
        else if(by.equalsIgnoreCase("attachments")){
            return group.SortbyAttachments();
        }
        else if(by.equalsIgnoreCase("date")){
            return group.SortbyDate();
        }
        else{
            throw new Error("Unprovided sorting: "+by);
        }
    }
}
