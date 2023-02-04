package com.example.mail.mail.filter;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.example.mail.mail.message;

public class CriteriaDate implements ICriteriaMail {
    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        ArrayList <message> cList=new ArrayList<>();
        for(message m:mails){
            String temp=m.getDate().toString();
            if(Pattern.compile(Pattern.quote((String)input),Pattern.CASE_INSENSITIVE).matcher(temp).find()){
                cList.add(m);
            }
        }
        return cList;
    }
    
}