package com.restbucks.ordering.model;

public class Grade {         // GRADE WILL HAVE GRADEITEM TYPE, GRADEITEM ID , GRADE AND FEEDBACK
    private int gradeID;
    private GradeItem gradeItem;
    private int gradeItemId;
    private double grade;
    private String feedback;

    public Grade(int gradeID, int gradeItemId, double grade, String feedback, GradeItem gradeItem) {
        this.gradeID = gradeID;
        this.gradeItem = gradeItem;
        this.gradeItemId = gradeItemId;
        this.grade = grade;
        this.feedback = feedback;
    }
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grade ID: " + gradeID + "\n" );
        sb.append("Grade Item: " + gradeItem + "\n");
        sb.append("Grade Item Id: " + gradeItemId + " \n");
        sb.append("Grade: " + grade);
        sb.append("Feedback: " + feedback + " \n");
        return sb.toString();
    }

    public GradeItem getGradeItem() {
        return gradeItem;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getGradeID() {
        return gradeID;
    }

    public int getGradeItemId() {
        return gradeItemId;
    }
}