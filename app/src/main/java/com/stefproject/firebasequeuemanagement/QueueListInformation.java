package com.stefproject.firebasequeuemanagement;

public class QueueListInformation {

    public String uid;
    public String queueId;
    public String queueListId;
    public String name;


    public QueueListInformation(){
    }

    public QueueListInformation(String uid, String queueId, String queueListId,String name){
        this.uid = uid;
        this.queueId = queueId;
        this.queueListId = queueListId;
        this.name = name;
    }
}
