package com.restbucks.ordering.representations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.model.GradeItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "grade", namespace = Representation1.APPEALS_NAMESPACE)
public class GradeRepresentation extends Representation1 {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradeRepresentation.class);
       
    @XmlElement(namespace = GradeRepresentation.APPEALS_NAMESPACE) private int gradeID;
    @XmlElement(namespace = GradeRepresentation.APPEALS_NAMESPACE) private GradeItem gradeItem;
    @XmlElement(namespace = GradeRepresentation.APPEALS_NAMESPACE) private int gradeItemId;
    @XmlElement(namespace = GradeRepresentation.APPEALS_NAMESPACE) private double grade;
    @XmlElement(namespace = GradeRepresentation.APPEALS_NAMESPACE) private String feedback;
    
    /**
     * For JAXB :-(
     */
     GradeRepresentation(){
        LOG.debug("In GradeRepresentation Constructor");
     }
    
     
    public GradeRepresentation(Grade storedGrade, Link1...links) {
        LOG.info("Creating an Grade Representation with the grade = {} and links = {}", grade, links);
        
        gradeID = storedGrade.getGradeID();
        gradeItem = storedGrade.getGradeItem();
        gradeItemId = storedGrade.getGradeItemId();
        grade = storedGrade.getGrade();
        feedback = storedGrade.getFeedback();
        this.links = java.util.Arrays.asList(links);
        
        LOG.debug("Created the Grade Representation {}", this);
    }

    public Grade getGrade() {
        return new Grade(gradeID,gradeItemId,grade,feedback,gradeItem);
    }
    
    public Link1 getAppealLink() {
        return getLinkByName(Representation1.RELATIONS_URI + "appeal");
    }
}