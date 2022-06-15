package com.stefproject.firebasequeuemanagement;

public class ManagerInformation {

    public String email;
    public String name;
    public String companyname;
    public String uid;
    public String companyUid;

    public ManagerInformation(){
    }
    public ManagerInformation(String name, String uid,String companyname, String email,String companyUid){
        this.name = name;
        this.uid = uid;
        this.email =email;
        this.companyname  =companyname;
        this.companyUid = companyUid;
    }
}
