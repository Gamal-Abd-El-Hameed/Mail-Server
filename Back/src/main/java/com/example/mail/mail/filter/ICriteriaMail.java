package com.example.mail.mail.filter;

import java.util.ArrayList;

import com.example.mail.mail.message;

public interface ICriteriaMail {
   ArrayList<message> meetCriteria(ArrayList<message> mails, Object input);
}