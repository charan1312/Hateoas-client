package com.restbucks.ordering.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

public class Appeal {
    
    private final int studentId;
    private final int gradeId;
    private List<String> comments;
    private String title;
    //@XmlTransient
    private AppealStatus appealStatus = AppealStatus.SUBMITTED;

    public Appeal(int sid, int gid, String title) {
      this(sid, gid, title, AppealStatus.SUBMITTED);
    }
    

    public Appeal(int sid, int gid, String title, AppealStatus appealStatus) {
        this.comments = new ArrayList<String>();
        this.gradeId = gid;
        this.studentId = sid;
        this.title = title;
        this.appealStatus = appealStatus;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {       // IS TO CHANGE THE TITLE BEFORE THE APPEAL IS SUBMITTED AND IT IS NOT IN-PROCESS YET
        if(this.appealStatus.equals(AppealStatus.SUBMITTED) || this.appealStatus.equals(AppealStatus.FOLLOWUP))
            this.title = title;
    }

    public int getStudentID() {          // FIXED STUDENT
        return studentId;
    }

    public int getGradeId() {           // FIXED GRADE
        return gradeId;
    }
    
    public void setAppealStatus(AppealStatus status) { 
        this.appealStatus = status;
    }

    public AppealStatus getStatus() {
        return appealStatus;
    }

    public void addComment(String comment) {         // add additional comments
        this.comments.add(comment);
    }
    
    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    
    public List<String> getComments() {         // add additional comments
        return this.comments;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: " + title + "\n" );
        sb.append("Student ID: " + studentId + "\n");
        sb.append("Grade ID: " + gradeId + "\n");
        sb.append("Status: " + appealStatus + " \n");
        sb.append("Comments: " + comments.toString());
        return sb.toString();
    }
}