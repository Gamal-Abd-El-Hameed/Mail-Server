package com.example.mail.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.example.mail.mail.message;
import com.example.mail.mail.messageGroup;
import com.example.mail.user.contact;
import com.example.mail.user.user;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Save {
    //create an object of SingleObject
    private static Save instance = new Save();
    private static String path=System.getProperty("user.home")+"//Downloads//System//tr.json";
    
   //make the constructor private so that this class cannot be
   //instantiated
    private Save(){
    
    }
      //Get the only object available
    public static Save getInstance(){

        return instance;
    }


    public JSONArray fromMessageGroubToJsonArray(String from,user u){
        Iterator<message> it=u.getGroup(from).getMessages().iterator(); 
        JSONArray messageG = new JSONArray();
  
        while( it.hasNext()){
            JSONObject in=new JSONObject();
            message m=it.next();
           in.put("from",m.getFrom());
           in.put("to",m.getTo());
           in.put("importance", m.getImportance());
           in.put("subject", m.getSubject());
           in.put("body", m.getBody());
           in.put("attach", m.getAttachments());
            in.put("date", m.getDate().toString());
            in.put("id", m.getId());

           messageG.add(in);
        }
        return messageG;
    }
    public  ArrayList<String> loadIds(){
        ArrayList<String> arr=new ArrayList<>();
        JSONParser jsonParser=new JSONParser();
            try{
                JSONArray tr  =(JSONArray) jsonParser.parse(new FileReader(System.getProperty("user.home")+"//Downloads//System//ids.json"));
                for(int i=0;i<tr.size();i++){
                    arr.add((String)tr.get(i));
                }
  

            }catch(Exception e){
                System.out.println(e);
                ObjectMapper objectMapper = new ObjectMapper();
                try {  
                    objectMapper.writeValue(new File(System.getProperty("user.home")+"//Downloads//System//ids.json"),arr);
                    System.out.println("created the file: ids.json");
                }                
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                return new ArrayList<String>();
            }
                return arr;
        
    }
    public void save(List<user> Array ) throws  IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray userToSave=new JSONArray();
        File f = new File(System.getProperty("user.home")+"//Downloads//System//tr.json");
        if(f.exists() && !f.isDirectory()) { 
            System.out.println(f.delete());
        }
        for(user u:Array){
            JSONObject obj=new JSONObject();
            obj.put("firstName",u.getFirstName()); 
            obj.put("lastName",u.getLastName());
            obj.put("gender",u.getGender());
            obj.put("email", u.getEmail());
            obj.put("password",u.getPassword());
            obj.put("contacts", u.getContacts());
            obj.put("inbox", fromMessageGroubToJsonArray("inbox",u));
            obj.put("draft", fromMessageGroubToJsonArray("draft",u));
            obj.put("trash", fromMessageGroubToJsonArray("trash",u));
            obj.put("sent", fromMessageGroubToJsonArray("sent",u));
            Enumeration<String> e = u.getFolders().keys();
 
            System.out.println(e);
            while (e.hasMoreElements()) {
                // Getting the key of a particular entry
                String key = e.nextElement();
                messageGroup group= u.getFolders().get(key);
                for(message m:group.getMessages()){

                    m.setDate(m.getDate().toString());
                

                }

            }
            obj.put("files",u.getFolders());

        userToSave.add(obj);
        }

        objectMapper.writeValue(new File(System.getProperty("user.home")+"//Downloads//System//tr.json"),userToSave);
            System.out.println("lol");


    }
    public ArrayList<contact> loadContacts(JSONArray jsArray){
        ArrayList<contact> answer=new ArrayList<>();
        for (int i = 0 ; i < jsArray.size(); i++) {
            JSONObject obj =(JSONObject)jsArray.get(i);
            contact c=new contact();
            String email=(String) obj.get("email");
            String name=(String) obj.get("name");
            c.setEmail(email);
            c.setName(name);
            answer.add(c);
        }
        return answer;
    }
    public user loadUser (JSONObject object){
        String first=(String) object.get("firstName");
        String last=(String) object.get("lastName");
        String gender=(String) object.get("gender");
        String email=(String) object.get("email");
        String password=(String) object.get("password");
        ArrayList<contact> contacts= loadContacts((JSONArray)object.get("contacts"));

       List<message> in= JsonArrayToMessageGroub((JSONArray)object.get("inbox"));
       List<message> dr= JsonArrayToMessageGroub((JSONArray)object.get("draft"));
       List<message> se= JsonArrayToMessageGroub((JSONArray)object.get("sent"));
       List<message> trashi= JsonArrayToMessageGroub((JSONArray)object.get("trash"));

        messageGroup inbox=new messageGroup(in);
        messageGroup draft=new messageGroup(dr);
        messageGroup sent=new messageGroup(se);
        messageGroup trash=new messageGroup(trashi);

        user user =new user(first, last, gender, email, password, inbox, sent, draft, trash);
        Hashtable<String, messageGroup> h=loadHash( (JSONObject) object.get("files"));
        user.setContacts(contacts);

        user.setFolders(h);
        return user;
    }
   
    private Hashtable<String, messageGroup> loadHash(JSONObject object) {
        Hashtable<String,messageGroup> has=new Hashtable<>();
            Iterator<String> keys = (Iterator<String>) object.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONObject ob=(JSONObject) object.get(key);
                // System.out.println("-----------------------");
                List<message> g=  JsonArrayToMessageGroub((JSONArray)ob.get("messages"));
                messageGroup gr=new messageGroup(g);
                has.put(key, gr);

            }
        return has;
    }
    public void saveIds(ArrayList<String> ids){
        try{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(System.getProperty("user.home")+"//Downloads//System//ids.json"),ids);
        }catch(Exception e){
            System.out.println(e);
        }
    } 
    public List<message> JsonArrayToMessageGroub(JSONArray jsArray){
        List<message> message =new ArrayList<>();
        // System.out.println(jsArray.size());
        for (int i = 0 ; i < jsArray.size(); i++) {
            JSONObject obj =(JSONObject)jsArray.get(i);
            String fromm = (String) obj.get("from");
            String to = (String) obj.get("to");

            int importance = Integer.parseInt((String) obj.get("importance"));

            String subject =  (String)obj.get("subject");

            String body= (String) obj.get("body");

            List<String> attach = (List<String>) obj.get("attach");
            String date =(String)obj.get("date");
            // System.out.println(date);
            LocalDateTime dateTime = LocalDateTime.parse(date);
            String id =(String)obj.get("id");

            message m= new message(fromm, to, subject, body, dateTime, importance,id);
            m.setAttachments(attach);
            message.add(m);

        }
        return message;
       
    }

    public List<user>load() throws  IOException, ParseException{
            JSONParser jsonParser=new JSONParser();
    ;
            
            List<user> u=new ArrayList<>();
            try{
                 JSONArray tr  =(JSONArray) jsonParser.parse(new FileReader(path));

            for(int i=0;i<tr.size();i++){
                u.add(loadUser((JSONObject)tr.get(i)));
            }
            System.out.println(u.size());

            return u;
        }catch(Exception e){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(System.getProperty("user.home")+"//Downloads//System//tr.json"),u);

            System.out.println("created file: tr.json");
        }
        return null;
    }

}
