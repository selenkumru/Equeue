package com.stefproject.firebasequeuemanagement;

public class QueueInformation {

    public String name;
    public String managerName;
    public String uid;
    public String queueId;
    public String companyUid;


    public QueueInformation(){
    }

    public QueueInformation(String name, String managerName,String uid,String queueId,String companyUid){
        this.name = name;
        this.managerName= managerName;
        this.uid = uid;
        this.queueId = queueId;
        this.companyUid = companyUid;
    }
}
