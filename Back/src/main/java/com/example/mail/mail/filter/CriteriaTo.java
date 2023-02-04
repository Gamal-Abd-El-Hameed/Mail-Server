package com.example.mail.mail.filter;

import java.util.ArrayList;

import com.example.mail.mail.message;

public class CriteriaTo implements ICriteriaMail{

    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        System.out.println("to");
        ArrayList <message> answer =new ArrayList<>();
        for(message m:mails){
            if(((String)input).equalsIgnoreCase(m.getTo())){
                answer.add(m);
            }
        }
        return answer;
    }
    
}