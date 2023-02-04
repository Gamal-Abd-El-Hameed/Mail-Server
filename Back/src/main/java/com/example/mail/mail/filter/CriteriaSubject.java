package com.example.mail.mail.filter;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.example.mail.mail.message;

public class CriteriaSubject implements  ICriteriaMail {

    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        input=((String)input).toLowerCase();
        ArrayList<message> answer=new ArrayList<>();
        for(message m:mails){
            if( Pattern.compile(Pattern.quote((String)input),Pattern.CASE_INSENSITIVE).matcher(m.getSubject()).find()){
            answer.add(m);
            }
        }
        return answer;
    }
    
}