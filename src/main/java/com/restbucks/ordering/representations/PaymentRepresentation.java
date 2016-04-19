package com.restbucks.ordering.representations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.restbucks.ordering.model.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "payment", namespace = Representation.RESTBUCKS_NAMESPACE)
public class PaymentRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(PaymentRepresentation.class);
       
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private double amount;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private String cardholderName;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private String cardNumber;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private int expiryMonth;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private int expiryYear;
    
    
    /**
     * For JAXB :-(
     */
     PaymentRepresentation(){
        LOG.debug("In PaymentRepresentation Constructor");
     }
    
    public PaymentRepresentation(Payment payment, Link...links) {
        LOG.info("Creating a Payment Representation with the payment = {} and links = {}", payment, links);
        
        amount = payment.getAmount();
        cardholderName = payment.getCardholderName();
        cardNumber = payment.getCardNumber();
        expiryMonth = payment.getExpiryMonth();
        expiryYear = payment.getExpiryYear();
        this.links = java.util.Arrays.asList(links);
        
        LOG.debug("Created the Payment Representation {}", this);
    }

    public Payment getPayment() {
        return new Payment(amount, cardholderName, cardNumber, expiryMonth, expiryYear);
    }
    
    public Link getReceiptLink() {
        return getLinkByName(Representation.RELATIONS_URI + "receipt");
    }
    
    public Link getOrderLink() {
        return getLinkByName(Representation.RELATIONS_URI + "order");
    }
}
