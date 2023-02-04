package com.example.mail.mail.filter;

public class criteraFactory {
    public ICriteriaMail getCriteria(String cri){
        if(cri.equalsIgnoreCase("Date")){
            return new CriteriaDate();
        }
        if(cri.equalsIgnoreCase("from")){
            return new CriteriaFrom();
        }
        if(cri.equalsIgnoreCase("to")){
            return new CriteriaTo();
        }
        if(cri.equalsIgnoreCase("importance")){
            return new CriteriaImportance();
        }
        if(cri.equalsIgnoreCase("subject")){
            return new CriteriaSubject();
        }
        if(cri.equalsIgnoreCase("body")){
            return new CriteriaBody();
        }
        return null;
    }


}