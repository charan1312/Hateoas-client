package com.restbucks.ordering.representations;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.restbucks.ordering.activities.InvalidGradeException;
import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.model.GradeItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "grade", namespace = Representation1.APPEALS_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class GradeRepresentation extends Representation1 {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradeRepresentation.class);
       
    @XmlElement(name = "gradeID",namespace = Representation1.APPEALS_NAMESPACE) 
    private int gradeID;
    @XmlElement(name = "gradeItem",namespace = Representation1.APPEALS_NAMESPACE) 
    private GradeItem gradeItem;
    @XmlElement(name = "gradeItemId",namespace = Representation1.APPEALS_NAMESPACE) 
    private int gradeItemId;
    @XmlElement(name = "grade",namespace = Representation1.APPEALS_NAMESPACE) 
    private double grade;
    @XmlElement(name = "feedback",namespace = Representation1.APPEALS_NAMESPACE) 
    private String feedback;
    
    /**
     * For JAXB :-(
     */
     GradeRepresentation(){
        LOG.debug("In GradeRepresentation Constructor");
     }
    
     public static GradeRepresentation fromXmlString(String xmlRepresentation) {
         LOG.info("Creating a Grade object from the XML = {}", xmlRepresentation);

         GradeRepresentation gradeRepresentation = null;     
         try {
             JAXBContext context = JAXBContext.newInstance(GradeRepresentation.class);
             Unmarshaller unmarshaller = context.createUnmarshaller();
             gradeRepresentation = (GradeRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
         } catch (Exception e) {
             throw new InvalidGradeException();
         }

         LOG.debug("Generated the object {}", gradeRepresentation);
         return gradeRepresentation;
     }

     
    public GradeRepresentation(Grade storedGrade, Link1...links) {
        LOG.info("Creating an Grade Representation with the grade = {} and links = {}", storedGrade.toString(), links.toString());
        
        gradeID = storedGrade.getGradeID();
        gradeItem = storedGrade.getGradeItem();
        gradeItemId = storedGrade.getGradeItemId();
        grade = storedGrade.getGrade();
        feedback = storedGrade.getFeedback();
        this.links = java.util.Arrays.asList(links);
        
        LOG.debug("Created the Grade Representation {}", this);
    }

    @Override
    public String toString() {
        LOG.info("Converting Grade Representation object to string");
        try {
            JAXBContext context = JAXBContext.newInstance(GradeRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public Grade getGrade() {
        return new Grade(gradeID,gradeItemId,grade,feedback,gradeItem);
    }
    
    public Link1 getUpdatedGradeLink() {
        return getLinkByName(Representation1.SELF_REL_VALUE);
    }
    
    
}