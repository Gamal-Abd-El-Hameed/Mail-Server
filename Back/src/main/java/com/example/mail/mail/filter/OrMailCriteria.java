package com.example.mail.mail.filter;

import java.util.ArrayList;

import com.example.mail.mail.message;

public class OrMailCriteria implements ICriteriaMail {
    private ICriteriaMail iCriteriaMail1;
    private ICriteriaMail iCriteriaMail2;
    OrMailCriteria(ICriteriaMail iCriteriaMail1,ICriteriaMail iCriteriaMail2){
        this.iCriteriaMail1=iCriteriaMail1;
        this.iCriteriaMail1=iCriteriaMail1;
    }

    @Override
    public ArrayList<message> meetCriteria(ArrayList<message> mails, Object input) {
        String arr[]=((String)input).split("-");
        ArrayList<message> answer1=iCriteriaMail1.meetCriteria(mails, arr[0]);
        ArrayList<message> answer2=iCriteriaMail2.meetCriteria(mails, arr[1]);
        for(message x : answer2)
		{
			if(!answer1.contains(x))
			{
				answer1.add(x);
			}
		}
        return answer1;
    }
}