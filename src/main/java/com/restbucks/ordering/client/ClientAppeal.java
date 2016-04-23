package com.restbucks.ordering.client;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.AppealStatus;
import com.restbucks.ordering.model.Item;
import com.restbucks.ordering.model.Location;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.model.OrderStatus;
import com.restbucks.ordering.representations.Representation1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation1.APPEALS_NAMESPACE)
public class ClientAppeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientAppeal.class);
    
    @XmlElement(name = "studentId", namespace = Representation1.APPEALS_NAMESPACE)
    private int studentId;
    
    @XmlElement(name = "gradeId", namespace = Representation1.APPEALS_NAMESPACE)
    private int gradeId;
    
    @XmlElement(name = "title", namespace = Representation1.APPEALS_NAMESPACE)
    private String title;

    @XmlElement(name = "comments", namespace = Representation1.APPEALS_NAMESPACE)
    private List<String> comments;
    
    @XmlElement(name = "appealStatus", namespace = Representation1.APPEALS_NAMESPACE)
    private AppealStatus appealStatus;
    
    private ClientAppeal(){}
    
    public ClientAppeal(Appeal appeal) {
        LOG.debug("Executing ClientAppeal constructor");
        this.studentId = appeal.getStudentID();
        this.gradeId = appeal.getGradeId();
        this.title = appeal.getTitle();
        this.comments = appeal.getComments();
    }
    
    public Appeal getAppeal() {
        LOG.debug("Executing ClientAppeal.getAppeal");
        return new Appeal(studentId, gradeId , title, appealStatus );
    }
    
    
    public int getStudentId() {
        return studentId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getComments() {
        return comments;
    }

    public AppealStatus getAppealStatus() {
        return appealStatus;
    }

    @Override
    public String toString() {
        LOG.debug("Executing ClientOrder.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientAppeal.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

}