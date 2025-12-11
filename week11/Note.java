package com.ceng.madlab.mynotesfirebase;

import com.google.firebase.firestore.Exclude;

import com.google.firebase.Timestamp;

public class Note {
    @Exclude // arayüzde tutuyorum ama database'e kaydetmiyorum
    private String id;
    private Timestamp date;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
