package com.example.mail.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import javax.el.ELException;

import com.example.mail.Service.GsonStrats.ExcludefromIn;
import com.example.mail.Service.GsonStrats.ExcludefromOut;
import com.example.mail.mail.GroupSorter;
import com.example.mail.mail.message;
import com.example.mail.mail.messageBuilder;
import com.example.mail.mail.messageGroup;
import com.example.mail.mail.filter.ICriteriaMail;
import com.example.mail.mail.filter.criteraFactory;
import com.example.mail.user.ContactsSorter;
import com.example.mail.user.contact;
import com.example.mail.user.user;
import com.example.mail.user.userBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mail")
// @CrossOrigin("http://localhost:4200")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class controller {
    GsonBuilder builder;
    Gson gson;
    final ExclusionStrategy Outstrat=new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }
        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            return (arg0.getAnnotation(ExcludefromOut.class)!=null);
        }   
    };
    final ExclusionStrategy Instrat=new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }
        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            return (arg0.getAnnotation(ExcludefromIn.class)!=null);
        }   
    };

    UserProxy Proxy;

    @Autowired
    public controller(){
        this.Proxy=new UserProxy();
        this.builder = new GsonBuilder().addSerializationExclusionStrategy(this.Outstrat).addDeserializationExclusionStrategy(this.Instrat); 
        this.builder.setPrettyPrinting(); 
        this.gson = builder.create();
        this.Proxy.uService.update();
    }
    @GetMapping("/{idStr}/contacts")
    public String getContacts(@PathVariable String idStr){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        return gson.toJson(x.getContacts());

    }
    @GetMapping("/{idStr}/contacts/{email}")
    public String getContact(@PathVariable String idStr,@PathVariable String email){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}

        user x= this.Proxy.getUserById(id);
        return gson.toJson(x.getContact(email));

    }

    @GetMapping("/{idStr}/addContact/{email}/{contactName}")
    public String addContact(@PathVariable String idStr,@PathVariable String email,@PathVariable String contactName){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        if(x.getContacts()==null){
            x.setContacts(new ArrayList<>());
        }       
            try{
                if(!this.Proxy.emailExists(email)){
                    return gson.toJson("email was not recognized");
                }
        if(x.IsContactWith(email)){
            return gson.toJson("email alrady contact");
        }
                contact newContact = new contact();
                newContact.setName(contactName);
                newContact.setEmail(email);
                x.addContact(newContact);
            }
            catch(Error e){
                return gson.toJson("email was not recognized");
            }
            this.Proxy.uService.update();
            return gson.toJson("Success");
        }
    @GetMapping("/{idStr}/renameContact/{email}/{newName}")
    public String renameContact(@PathVariable String idStr,@PathVariable String email,@PathVariable String newName){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
            try{
                x.renameContact(email, newName);
            }
            catch(Error e){
                return gson.toJson(e.getMessage());
            }
            this.Proxy.uService.update();
            return gson.toJson("Success");
        }
    @DeleteMapping("/{idStr}/deleteContact/{email}")
    public String deleteContact(@PathVariable String idStr,@PathVariable String email){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
            try{
                x.deleteContact(email);
            }
            catch(Error e){
                return gson.toJson(e.getMessage());
            }
            this.Proxy.uService.update();

            return gson.toJson("Success");
        }
   
    // @PutMapping("/{idStr}/newMessage/draft")
    // public String draftMessage(@PathVariable String idStr,@RequestBody String msg){
    //     UUID id=UUID.fromString(idStr);
    //     if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
    //     user x= this.Proxy.getUserById(id);
    //     message newMessage=new Gson().fromJson(msg, message.class);
    //     messageBuilder.build(newMessage);
    //     newMessage.setFrom(x.getEmail());
    //     x.addMessage("draft", newMessage);
    //     this.Proxy.uService.update();

    //     return gson.toJson("Success");
    // }
    @PostMapping("/{idStr}/compose/send")
    public String sendMessage(@PathVariable String idStr,@RequestBody String msg){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        message newMessage=new Gson().fromJson(msg, message.class);
        messageBuilder.build(newMessage);
        newMessage.setFrom(x.getEmail());
        try{
            user y=this.Proxy.getUserbyEmail_notValidated(newMessage.getTo());
            y.addMessage("inbox", newMessage);
            x.addMessage("sent", newMessage);
        }
        catch(Error e){
            for (message i : x.getGroup("draft").getMessages()) {
                if(i.getSubject().equals(newMessage.getSubject())&&i.getBody().equals(newMessage.getBody())&&i.getTo().equals(newMessage.getTo())){
                    return gson.toJson("");
                }
            }
            x.addMessage("draft", newMessage);
            this.Proxy.uService.update();
            return gson.toJson("");
        }
        this.Proxy.uService.update();
        return gson.toJson("Success");
    }
    @PostMapping("/{idStr}/{A}/copyto/{B}")
    public String moveMessages(@PathVariable String idStr,@PathVariable String A,@PathVariable String B,@RequestBody String indices ){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        Type typeOfT=new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> arr =gson.fromJson(indices, typeOfT);
        message m;
        try{
            messageGroup GA=x.getGroup(A);
            messageGroup GB=x.getGroup(B);
            for(int i=0;i<arr.size();i++){
                m=GA.getMessage(arr.get(i));
                GB.addMessage(m.clone());
            }
            this.Proxy.uService.update();
        }
        catch(Error e){
            return "Some thing went wrong";
        }
        return "Success";
    }
   
    @GetMapping("/{idStr}/{group}")
    public String getGroup(@PathVariable String idStr,@PathVariable String group){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised ID");}
        try{
            messageGroup Group=this.Proxy.getUserById(id).getGroup(group);
            return gson.toJson(Group.getMessages());
        }
        catch(Error e){
            return e.getMessage();
        }
    }

    @GetMapping("/{idStr}/{group}/{idmessage}")
    public String getMessage(@PathVariable String idStr,@PathVariable String group,@PathVariable String idmessage){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised ID");}
        messageGroup Group=this.Proxy.getUserById(id).getGroup(group);
        return gson.toJson(Group.getMessage(idmessage));
    }

    @DeleteMapping("/{idStr}/{group}/{idm}/delete")
    public String deleteMessage(@PathVariable String idStr,@PathVariable String group,@PathVariable String idm){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        message tobedeleted = x.getGroup(group).getMessage(idm);
        x.getGroup(group).deleteMessage(idm);
        message newMessage = tobedeleted.clone();
        messageBuilder.build(newMessage);
        if(group!="trash"){
            x.addMessage("trash", newMessage);
        }
        this.Proxy.uService.update();

        return gson.toJson("Success");
    }
    @GetMapping("/{idStr}/{group}/{idm}/setImportance/{imp}")
    public String changeMessageImp(@PathVariable String idStr,@PathVariable String group,@PathVariable String idm,@PathVariable int imp){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        message m = x.getGroup(group).getMessage(idm);
        m.setImportance(imp);
        this.Proxy.uService.update();
        return gson.toJson("Success");
    }
    @PostMapping("/{idStr}/{group}/Mdelete")
    public String deleteMultipleMessages(@PathVariable String idStr,@PathVariable String group,@RequestBody String indices){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x= this.Proxy.getUserById(id);
        messageGroup G=x.getGroup(group);
        Type typeOfT=new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> arr =gson.fromJson(indices, typeOfT);
        message tobedeleted,newMessage;
        for(int i=0;i<arr.size();i++){
            tobedeleted = x.getGroup(group).getMessage(arr.get(i));
            newMessage =(tobedeleted).clone();
            G.deleteMessage(arr.get(i));
            messageBuilder.build(newMessage);
            if(!(group.equals("trash"))){
                x.addMessage("trash", newMessage);
            }
            this.Proxy.uService.update();
        }
        return gson.toJson("Success");
    }
  
    @GetMapping("/{idStr}/{group}/sort/{bywhat}")
    public String SortGroup(@PathVariable String idStr,@PathVariable String group,@PathVariable String bywhat){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        messageGroup g=this.Proxy.getUserbyIndex(this.Proxy.getIndexById(id)).getGroup(group);
        try{
            return gson.toJson(GroupSorter.SortGroup(g, bywhat).getMessages());
        } 
        catch(Error e){
            return gson.toJson(e.getMessage());
        }
    }
    @GetMapping("/{idStr}/contacts/sort/{bywhat}")
    public String SortContacts(@PathVariable String idStr,@PathVariable String group,@PathVariable String bywhat){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return gson.toJson("Unrecognised Id");}
        user x=this.Proxy.getUserbyIndex(this.Proxy.getIndexById(id));
        try{
            return gson.toJson(ContactsSorter.SortGroup(x, bywhat));
        } 
        catch(Error e){
            return gson.toJson(e.getMessage());
        }
    }
    @GetMapping("/{idStr}/{Group}/filter/{criteria}")
    public String getFilter(@PathVariable String idStr , @PathVariable String Group,
    @PathVariable String criteria,
    @RequestParam (value="value") String value ){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised ID";}
        messageGroup group=this.Proxy.getUserbyIndex(this.Proxy.getIndexById(id)).getGroup(Group);
        if(group==null){
            group =new messageGroup();
            return gson.toJson(group.getMessages());
        }
        ICriteriaMail iCriteriaMail = new criteraFactory().getCriteria(criteria);
        
        return gson.toJson(iCriteriaMail.meetCriteria((ArrayList<message>)group.getMessages(),value));
    }
    @GetMapping("{idStr}/{group}/search/{text}")
    public String search(@PathVariable String idStr , @PathVariable String Group,@PathVariable String text){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised ID";}
        messageGroup group=this.Proxy.getUserbyIndex(this.Proxy.getIndexById(id)).getGroup(Group);
        return gson.toJson(group.search(text).getMessages());
    }
    @GetMapping("{idStr}/contacts/search/{text}")
    public String searchContact(@PathVariable String idStr , @PathVariable String Group,@PathVariable String text){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised ID";}
        return gson.toJson(this.Proxy.getUserById(id).search(text));
    }
    @GetMapping("/{idStr}/{Group}/filter/{criteria}/{to}")
    public String getFilterTo(@PathVariable String idStr , @PathVariable String Group, @PathVariable String to
    ,@PathVariable String criteria,
    @RequestParam (value="value") String value){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised ID";}
        user x=this.Proxy.getUserbyIndex(this.Proxy.getIndexById(id));
        messageGroup groupfrom=x.getGroup(Group);
        messageGroup groupto=x.getGroup(to);
        ICriteriaMail iCriteriaMail = new criteraFactory().getCriteria(criteria);  
        if(groupto==null){
            groupto=new messageGroup(iCriteriaMail.meetCriteria((ArrayList<message>)groupfrom.getMessages(),value));
            x.getFolders().put(to,groupto);
        }
        else{
            if(Group.equals(to)){
                return gson.toJson(iCriteriaMail.meetCriteria((ArrayList<message>)groupfrom.getMessages(),value));
            }
            else{
               ArrayList<message> cr= iCriteriaMail.meetCriteria((ArrayList<message>)groupfrom.getMessages(),value);
                x.addTomessageGroup(cr,groupto);
            }
        }
        this.Proxy.uService.update();
        return gson.toJson(iCriteriaMail.meetCriteria((ArrayList<message>)groupto.getMessages(),value));
    }
    

    @GetMapping("/login")
    public String login(@RequestParam (value="email") String email,@RequestParam(value = "password") String password){
        ArrayList<Object>x=new ArrayList<>();
        try {
            user user=this.Proxy.getUserbyEmail_Validated(email, password);
            Integer index=this.Proxy.sessionExistsbyEmail(email);
            UUID id;
            if(index!=null){
                id=(this.Proxy.getIdByIndex(index));
                this.Proxy.removeSession(id);
            }
            id=this.Proxy.addSession(this.Proxy.getIndexByEmail(email));
            x.add(id);
            x.add(user.getFirstName());
            x.add(user.getLastName());
            x.add(user.getGender());
            x.add(user.getFolders().keySet().toArray());
            user.cleanTrash();
            this.Proxy.uService.update();
            return gson.toJson(x);
        }
        catch(Error e){
            return gson.toJson("");
        }
    }
    
    @GetMapping("/signup/isAvail")
    public boolean isAvail(@RequestParam (value="email") String email){
        return this.Proxy.uService.isEmailAvail(email);
    }
    @PostMapping("/signup")
    public String signup (@RequestBody String userJson){
        user newUser=new Gson().fromJson(userJson, user.class);
        if(this.Proxy.uService.isEmailAvail(newUser.getEmail())){
            try{
                userBuilder.buildNewUser(newUser);
                UUID id =this.Proxy.addNewUser(newUser);
                this.Proxy.uService.update();
                return gson.toJson(id);
            }
            catch(Error e){
                return gson.toJson("");
            }
        }
        else{
            return gson.toJson("");
        }
    }
    @PostMapping("/{idStr}/editAccount")
    public String editUser(@PathVariable String idStr,@RequestBody String newUserJson){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised Id";}
        user x= this.Proxy.getUserById(id);
        user newUser=gson.fromJson(newUserJson, user.class);
        x.update(newUser);
        return"Success";
    }
    @PostMapping("/{idStr}/deleteAccount")
    public String deleteUser(@PathVariable String idStr){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised Id";}
        this.Proxy.deleteUser(id);
        return"Success";
    }
    @DeleteMapping("/{idStr}/{folder}/delete")
    public String deleteFolder(@PathVariable String idStr,@PathVariable String folder){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised Id";}
        user x= this.Proxy.getUserById(id);
        if(!x.getFolders().keySet().contains(folder)){
            return "folder: '"+ folder +"' doesn't exist";
        }
        x.deleteFolder(folder);
        return "Success";
    }
    @GetMapping("/{idStr}/{folder}/renameto/{newName}")
    public String renameFolder(@PathVariable String idStr,@PathVariable String folder,@PathVariable String newName){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return "Unrecognised Id";}
        user x= this.Proxy.getUserById(id);
        if(!x.getFolders().keySet().contains(folder)){
            return "folder: '"+ folder +"' doesn't exist";
        }
        x.renameFolder(folder, newName);
        return "Success";
    }
    @GetMapping("/{idStr}/newGroup/{name}")
    public boolean create(@PathVariable String name,@PathVariable String idStr){
        UUID id=UUID.fromString(idStr);
        if(!this.Proxy.IDexists(id)){return false;}
        user x= this.Proxy.getUserById(id);
        try{
            if(x.getGroup(name)!=null){
                return false;
            }
            Hashtable<String,messageGroup> y=x.getFolders();
            
            System.out.println(name);
            if(y.containsKey(name)){
                return false;
            }
            y.put(name, new messageGroup());
            System.out.println(y.containsKey(name));
            this.Proxy.uService.update();
            return true;
        }catch(ELException e){
            System.out.println(e);
        }
        return false;
    }
    
    @GetMapping("/admin/3AMO")
    public String admin(){
        return gson.toJson(Proxy.uService.users);
    }
}
