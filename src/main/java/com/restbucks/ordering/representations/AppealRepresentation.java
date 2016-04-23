package com.restbucks.ordering.representations;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restbucks.ordering.activities.InvalidAppealException;
import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.AppealStatus;

@XmlRootElement(name = "appeal", namespace = Representation1.APPEALS_NAMESPACE)
public class AppealRepresentation extends Representation1 {

    private static final Logger LOG = LoggerFactory.getLogger(AppealRepresentation.class);

    @XmlElement(name = "studentId", namespace = Representation1.APPEALS_NAMESPACE)
    private int studentId;

    @XmlElement(name = "gradeId", namespace = Representation1.APPEALS_NAMESPACE)
    private int gradeId;

    @XmlElement(name = "comment", namespace = Representation1.APPEALS_NAMESPACE)
    private List<String> comments;

    @XmlElement(name = "title", namespace = Representation1.APPEALS_NAMESPACE)
    private String title;

    @XmlElement(name = "status", namespace = Representation1.APPEALS_NAMESPACE)
    private AppealStatus appealStatus;

    /**
     * For JAXB :-(
     */
    AppealRepresentation() {
        LOG.debug("In AppealRepresentation Constructor");
    }

    public static AppealRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);

        AppealRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }

        LOG.debug("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }

    public static AppealRepresentation createResponseAppealRepresentation(Appeal appeal, AppealsUri appealUri) {
        LOG.info("Creating a Response Appeal for appeal = {} and Appeal URI", appeal.toString(), appealUri.toString());

        AppealRepresentation appealRepresentation;     

        //AppealsUri gradeUri = new AppealsUri(appealUri.getBaseUri() + "/grade/" + appealUri.getId().toString());
        AppealsUri gradeUri = new AppealsUri(appealUri.getBaseUri() + "/grade/" + appeal.getGradeId());
        LOG.debug("Grade URI = {}", gradeUri);

//        if(appeal.getStatus() == AppealStatus.CREATED) {
//            LOG.debug("The appeal status is {}", AppealStatus.CREATED);
//            appealRepresentation = new AppealRepresentation(appeal, 
//                    new Link1(RELATIONS_URI + "submit", appealUri),
//                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
//        } else 
        if(appeal.getStatus() == AppealStatus.SUBMITTED) {
            LOG.debug("The appeal status is {}", AppealStatus.SUBMITTED);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link1(RELATIONS_URI + "delete", appealUri),
                    new Link1(RELATIONS_URI + "process", appealUri),
                    new Link1(RELATIONS_URI + "followup", appealUri),
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.FOLLOWUP) {
            LOG.debug("The appeal status is {}", AppealStatus.FOLLOWUP);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link1(RELATIONS_URI + "process", appealUri),
                    new Link1(RELATIONS_URI + "delete", appealUri),
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.INPROCESS) {
            LOG.debug("The appeal status is {}", AppealStatus.INPROCESS);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link1(RELATIONS_URI + "grade", gradeUri),
                    new Link1(RELATIONS_URI + "reject", appealUri), 
//                    new Link1(RELATIONS_URI + "approve", appealUri),
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.UPDATEGRADE) {
            LOG.debug("The appeal status is {}", AppealStatus.UPDATEGRADE);
            appealRepresentation = new AppealRepresentation(appeal,
                    new Link1(RELATIONS_URI + "approve", appealUri), 
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.APPROVED) {
            LOG.debug("The appeal status is {}", AppealStatus.APPROVED);
            appealRepresentation = new AppealRepresentation(appeal,
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.REJECTED) {
            LOG.debug("The appeal status is {}", AppealStatus.REJECTED);
            appealRepresentation = new AppealRepresentation(appeal,
                    new Link1(Representation1.SELF_REL_VALUE, appealUri)); 
        } else if(appeal.getStatus() == AppealStatus.DELETED) {
            LOG.debug("The appeal status is {}", AppealStatus.DELETED);
            appealRepresentation = new AppealRepresentation(appeal,
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));            
        } else {
            LOG.debug("The appeal status is in an unknown status");
            throw new RuntimeException("Unknown Order Status");
        }

        LOG.debug("The appeal representation created for the Create Response Appeal Representation is {}", appealRepresentation);

        return appealRepresentation;
    }

    public AppealRepresentation(Appeal appeal, Link1... links) {
        LOG.info("Creating an Appeal Representation for order = {} and links = {}", appeal.toString(), links.toString());

        try {
            this.studentId = appeal.getStudentID();
            this.comments = appeal.getComments();
            this.title = appeal.getTitle();
            this.appealStatus = appeal.getStatus();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }

        LOG.debug("Created the AppealRepresentation {}", this);
    }

    @Override
    public String toString() {
        LOG.info("Converting Appeal Representation object to string");
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appeal getAppeal() {
        LOG.info("Retrieving the Appeal Representation");
        if (title.isEmpty() || gradeId == 0 || studentId ==0 ) {
            throw new InvalidAppealException();
        }

        Appeal appeal = new Appeal(studentId, gradeId, title);

        LOG.debug("Retrieving the Appeal Representation {}", appeal);

        return appeal;
    }

//    public Link1 getSubmitLink() {
//        LOG.info("Retrieving the Submit link of appeal");
//        return getLinkByName(RELATIONS_URI + "submit");
//    }

    public Link1 getGradeLink() {
        LOG.info("Retrieving the Grade link ");
        return getLinkByName(RELATIONS_URI + "grade");
    }

    public Link1 getUpdateLink() {
        LOG.info("Retrieving the Update Appeal link ");
        return getLinkByName(RELATIONS_URI + "update");
    }

    public Link1 getApproveLink() {
        LOG.info("Retrieving the Approve link ");
        return getLinkByName(RELATIONS_URI + "approve");
    }

    public Link1 getRejectLink() {
        LOG.info("Retrieving the Reject Appeal link ");
        return getLinkByName(RELATIONS_URI + "reject");
    }

    public Link1 getDeleteLink() {
        LOG.info("Retrieving the Delete Appeal link ");
        return getLinkByName(RELATIONS_URI + "delete");
    }

    public Link1 getProcessLink() {
        LOG.info("Retrieving the IN-Process for appeal link ");
        return getLinkByName(RELATIONS_URI + "process");
    }

    public Link1 getSelfLink() {
        LOG.info("Retrieving the Self link of appeal");
        return getLinkByName("self");
    }

    public Link1 getFollowUpLink() {
        LOG.info("Retrieving the FollowUp for appeal link ");
        return getLinkByName(RELATIONS_URI + "followup");
    }

    public AppealStatus getStatus() {
        LOG.info("Retrieving the appeal status {}", appealStatus);
        return appealStatus;
    }

    public String getTitle() {
        LOG.info("Retrieving the appeal title {}", title);
        return title;
    }
}