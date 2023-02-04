package com.example.mail.mail.filter;

import java.util.ArrayList;

import com.example.mail.mail.message;

public class AndMailCriteria implements ICriteriaMail{
    private ICriteriaMail iCriteriaMail1;
    private ICriteriaMail iCriteriaMail2;
    AndMailCriteria(ICriteriaMail iCriteriaMail1,ICriteriaMail iCriteriaMail2){
        this.iCriteriaMail1=iCriteriaMail1;
        this.iCriteriaMail1=iCriteriaMail1;
    }

    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        String arr[]=((String)input).split("and");
        ArrayList<message> answer=iCriteriaMail1.meetCriteria(mails, arr[0]);
        answer=iCriteriaMail2.meetCriteria(answer, arr[1]);
        
        return answer;
    }
    
}