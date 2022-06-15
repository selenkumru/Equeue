package com.stefproject.firebasequeuemanagement;

public class Userinformation {

    public String email;
    public String name;
    public String surname;
    public String phoneno;
    public String uid;

    public Userinformation(){
    }

    public Userinformation(String name,String surname, String phoneno,String uid,String email){
        this.name = name;
        this.surname = surname;
        this.phoneno = phoneno;
        this.uid = uid;
        this.email =email;
    }
}
