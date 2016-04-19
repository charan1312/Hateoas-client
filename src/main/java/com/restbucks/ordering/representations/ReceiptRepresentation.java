package com.restbucks.ordering.representations;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.restbucks.ordering.model.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "receipt", namespace = Representation.RESTBUCKS_NAMESPACE)
public class ReceiptRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReceiptRepresentation.class);

    @XmlElement(name = "amount", namespace = Representation.RESTBUCKS_NAMESPACE)
    private double amountPaid;
    @XmlElement(name = "paid", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String paymentDate;
    
    ReceiptRepresentation(){
        LOG.debug("In ReceiptRepresentation Constrictor");
    } // For JAXB :-(
    
    public ReceiptRepresentation(Payment payment, Link orderLink) {
        LOG.info("Creating an Receipt Representation with the payment = {} and links = {}", payment, links);
        
        this.amountPaid = payment.getAmount();
        this.paymentDate = payment.getPaymentDate().toString();
        this.links = new ArrayList<Link>();
        links.add(orderLink);
        
        LOG.debug("Created the Receipt Representation {}", this);
    }

    public DateTime getPaidDate() {
        return new DateTime(paymentDate);
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }

    public Link getOrderLink() {
        return getLinkByName(Representation.RELATIONS_URI + "order");
    }
    
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(ReceiptRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
