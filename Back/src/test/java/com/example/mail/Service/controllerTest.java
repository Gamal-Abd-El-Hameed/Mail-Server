package com.example.mail.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import com.example.mail.Service.GsonStrats.ExcludefromIn;
import com.example.mail.Service.GsonStrats.ExcludefromOut;
import com.example.mail.mail.message;
import com.example.mail.mail.messageBuilder;
import com.example.mail.mail.messageGroup;
import com.example.mail.user.user;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

class controllerTest {
    @InjectMocks
    @Autowired
    controller underTest;
    user test;
    ObjectMapper mapper ;
    Gson gson;
    GsonBuilder builder;
    
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
    UserProxy Proxy=new UserProxy();
    String id;
   @BeforeEach
    public void init() throws JsonProcessingException{
        messageGroup in=new messageGroup();
        messageGroup dr=new messageGroup();
        messageGroup sent=new messageGroup();
        messageGroup trash=new messageGroup();
        this.Proxy=new UserProxy();
        underTest = new controller();
        mapper = new ObjectMapper();
        this.builder = new GsonBuilder().addSerializationExclusionStrategy(this.Instrat).addDeserializationExclusionStrategy(this.Outstrat); 
        this.builder.setPrettyPrinting(); 
        this.gson = builder.create();
        if(underTest.Proxy.emailExists("email@mail.com")){
            test = new user("amr", "magdy","male", "email@mail.com" ,"password0lol", in, sent, dr, trash);
             underTest.signup(mapper.writeValueAsString(test));
        }else{
            test = new user("amr", "magdy","male", "email@mail.com" ,"password0lol", in, sent, dr, trash);
            underTest.signup(mapper.writeValueAsString(test));
        }
    }
    @Test
    void signup() throws JsonProcessingException {
        // controller underTest = new controller();
        messageGroup in=new messageGroup();
        messageGroup dr=new messageGroup();
        messageGroup sent=new messageGroup();
        messageGroup trash=new messageGroup();
        mapper = new ObjectMapper();

        user test = new user("yousef", "saber", "male", "email@Youuusef.com", "password0lo", in, sent, dr, trash);
        
        if(underTest.Proxy.emailExists("email@Youuusef.com")){
            String resulte =underTest.signup(mapper.writeValueAsString(test));
            assertEquals(resulte,mapper.writeValueAsString( ""));
        }else{
            String resulte =underTest.signup(mapper.writeValueAsString(test));
            assertNotEquals(resulte,mapper.writeValueAsString( "email has been taken"));
        }

        System.out.println(test);
        String resulte =underTest.signup(mapper.writeValueAsString(test));
        assertEquals(resulte,mapper.writeValueAsString( "email has taken"));

        test = new user("amr", "magdy", null, null ,"password0lol", in, sent, dr, trash);
        resulte =underTest.signup(mapper.writeValueAsString(test));
        assertEquals(resulte,mapper.writeValueAsString( "missing info"));


    }
    @Test
    void login() throws JsonProcessingException {
        // controller underTest = new controller();
        String result =underTest.login("email@mail.com", "password0lol");
        id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        mapper = new ObjectMapper();
        assertNotEquals(mapper.writeValueAsString( "Incorrect info"), result);
        result =underTest.login("email@mail.com", "125");
        assertEquals(mapper.writeValueAsString( "Incorrect info"), result);
    }
    
    @Test
    void getContacts() throws JsonProcessingException {
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","").replace(" ", "");
        id=id.substring(2,id.length()-1);
        // assertEquals(mapper.writeValueAsString("Success"), id);
        
        String res=underTest.getContacts(id);
        UUID idStr=UUID.fromString(id);
        user x= this.underTest.Proxy.getUserById(idStr);
        if(x.getContacts().size()==0){
            assertEquals(res,"[]" );
        }else{
                   assertNotEquals(res,"[]" );
        }
 
        // res=underTest.addContact(id, "email@Youuusef.com","yousef");
        // assertNotEquals(mapper.writeValueAsString("Success"), res);

        // assertNotEquals(res,"[]" );
    }
    @Test
    void addContact(){
        String res;
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","").replace(" ", "");
        id=id.substring(2,id.length()-1);
        UUID idStr=UUID.fromString(id);
        underTest.addContact(id, "email@Youuusef.com", "Elsaber");
        user x= this.underTest.Proxy.getUserById(idStr);
        boolean resul= x.IsContactWith("email@Youuusef.com");
        assertTrue(resul);
    }
    

    @Test
    void renameContact() throws JsonProcessingException {
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
       String res=  underTest.renameContact(id, "email@Youuusef.com", "Elsaber");
        // assertEquals(mapper.writeValueAsString("Success"), id);
        UUID idStr=UUID.fromString(id);
        user x= this.underTest.Proxy.getUserById(idStr);
        if(x.IsContactWith("email@Youuusef.com")){
            assertEquals(x.getContact("email@Youuusef.com").getName(),"Elsaber");
        }

    }
    @Test
    void draftMessage() throws JsonProcessingException {
        message sent2=new message("3amoooo", "hi", "body is hiiii",2);
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        // String res=underTest.draftMessage(id,gson.toJson(sent2));
        // assertEquals(mapper.writeValueAsString("Success"), res);
    }
    @Test
    void sendMessage() throws JsonProcessingException {
        message sent2=new message("3amooo", "hi", "body is hiiii",2);
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        String res=underTest.sendMessage(id,gson.toJson(sent2));
        // assertEquals(mapper.writeValueAsString(""), res);
        sent2=new message("email@Youuusef.com", "hii", "body is hiiii",2);
        res=underTest.sendMessage(id,gson.toJson(sent2));
        assertEquals(mapper.writeValueAsString("Success"), res);
    }
    @Test
    void deleteContact() throws JsonProcessingException {
        message sent2=new message("3amooo", "hi", "body is hiiii",2);
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        String res=underTest.deleteContact(id, "email@Youuusef.com");
        // assertEquals(mapper.writeValueAsString("Success"), res);
        UUID idStr=UUID.fromString(id);
        user x= this.underTest.Proxy.getUserById(idStr);
        boolean resul= x.IsContactWith("email@Youuusef.com");
        assertFalse(resul);
    }
    @Test
    void getGroup() {
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        UUID idStr=UUID.fromString(id);
        if(underTest.Proxy.getUserById(idStr).getGroup("0")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("0").size()!=0){
                String result=underTest.getGroup(id, "0");
                assertNotEquals(result, "[]");
            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.getGroup(id, "0");
                assertEquals(result, "[]");
            }
        }
        if(underTest.Proxy.getUserById(idStr).getGroup("sent")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("sent").size()!=0){
                String result=underTest.getGroup(id, "sent");
                assertNotEquals(result, "[]");
            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.getGroup(id, "sent");
                assertEquals(result, "[]");
            }
        }
    }

    @Test
    void sortGroup() {
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        UUID idStr=UUID.fromString(id);
        if(underTest.Proxy.getUserById(idStr).getGroup("0")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("0").size()!=0){
                String result=underTest.SortGroup(id, "0","importance");
                assertNotEquals(result, "[]");
            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.SortGroup(id, "0","importance");
                assertEquals(result, "[]");
            }
            if(underTest.Proxy.getUserById(idStr).getGroup("inbox")!=null){
                if(underTest.Proxy.getUserById(idStr).getGroup("inbox").size()!=0){
                    String result=underTest.SortGroup(id, "inbox","importance");
                    assertNotEquals(result, "[]");
                }
                else{
                    // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                    String result=underTest.SortGroup(id, "inbox","importance");
                    assertEquals(result, "[]");
                }
        }
        if(underTest.Proxy.getUserById(idStr).getGroup("sent")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("sent").size()!=0){
                String result=underTest.getGroup(id, "sent");
                assertNotEquals(result, "[]");
            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.getGroup(id, "sent");
                assertEquals(result, "[]");
            }
         }
        }
    }


    @Test
    void getFilter() {
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        UUID idStr=UUID.fromString(id);
        if(underTest.Proxy.getUserById(idStr).getGroup("0")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("0").size()!=0){
                String result=underTest.getFilter(id,"importance", "0","1");
                assertNotEquals(result, "[]");
            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.getFilter(id,"importance", "0","1");
                assertEquals(result, "[]");
            }
            if(underTest.Proxy.getUserById(idStr).getGroup("inbox")!=null){
                if(underTest.Proxy.getUserById(idStr).getGroup("inbox").size()!=0){
                    String result=underTest.getFilter(id,"importance", "0","1");
                    assertNotEquals(result, "[]");
                }
                else{
                    // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                    String result=underTest.getFilter(id,"importance", "0","1");
                    assertEquals(result, "[]");
                }
        }
        if(underTest.Proxy.getUserById(idStr).getGroup("sent")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("sent").size()!=0){
                String result=underTest.getFilter(id,"importance", "sent","1");
                assertNotEquals(result, "[[]]");
            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.getFilter(id,"importance", "sent","1");
                assertEquals(result, "[]");
            }
         }
        }
    }

    @Test
    void getFilterTo() {
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        UUID idStr=UUID.fromString(id);
        if(underTest.Proxy.getUserById(idStr).getGroup("sent")!=null){
            if(underTest.Proxy.getUserById(idStr).getGroup("sent").size()!=0){
                String result=underTest.getFilterTo(id, "sent","to","importance","1");
                messageGroup to= underTest.Proxy.getUserById(idStr).getGroup("to");
                assertNotEquals(null, to);

            }
            else{
                // assertEquals(underTest.Proxy.getUserById(idStr).getGroup("0").size(), 1);
                String result=underTest.getFilter(id,"importance", "sent","1");
                assertEquals(result, "[]");
            }
         }
        }
    
    @Test
    void create() throws JsonProcessingException {
        // MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new controller()).build();
		// mockMvc.perform(get("/hi/3"))
		// 		.andExpect(status().isOk())
		// 		.andExpect(content().string("ok"));
		// mockMvc.perform(get("/hi/3/month/4"))
		// 		.andExpect(status().isNotFound());
        String id =underTest.login("email@mail.com", "password0lol").split(",")[0];
        id=id.replaceAll("\\[|","");
        id=id.substring(4,id.length()-1);
        UUID idStr=UUID.fromString(id);

        // assertEquals(id, "hi");
        if(underTest.Proxy.getUserById(idStr).getGroup("0")==null){
            boolean result =underTest.create("0",id);
            assertTrue(result);


        }else{
            boolean result =underTest.create("0",id);
            assertFalse(result);
        }

    }
}