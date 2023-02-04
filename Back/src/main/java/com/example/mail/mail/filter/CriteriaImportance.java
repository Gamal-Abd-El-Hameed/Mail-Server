package com.example.mail.mail.filter;

import java.util.ArrayList;

import com.example.mail.mail.message;

public class CriteriaImportance implements ICriteriaMail {
    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        ArrayList<message> answer = new ArrayList<>();
        if(input instanceof String){
            input=Integer.parseInt((String)input);
        }
        for(message m:mails){
            if((int)input==Integer.parseInt(m.getImportance())){
                answer.add(m);
            }
        }
        return answer;
    }
}