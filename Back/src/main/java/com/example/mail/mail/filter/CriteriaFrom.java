package com.example.mail.mail.filter;

import java.util.ArrayList;

import com.example.mail.mail.message;

public class CriteriaFrom implements ICriteriaMail {

    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        ArrayList <message> answer =new ArrayList<>();
        for(message m:mails){
            if(((String)input).equalsIgnoreCase(m.getFrom())){
                answer.add(m);
            }
        }
        return answer;
    }


    
}