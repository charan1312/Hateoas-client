package com.restbucks.ordering.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="Submitted")          //for follow-up
    SUBMITTED,
    @XmlEnumValue(value="Approved")          //happy case
    APPROVED, 
    @XmlEnumValue(value="InProcess")          //processing phase
    INPROCESS, 
    @XmlEnumValue(value="Rejected")               //appeal rejected case
    REJECTED,
    @XmlEnumValue(value="Deleted")              // student abandoned case
    DELETED, 
    @XmlEnumValue(value="Created")              //default value when created
    CREATED
}
