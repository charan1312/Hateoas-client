package com.restbucks.ordering.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum GradeItem {
    @XmlEnumValue(value="Assignment")
    ASSIGNMENT,
    @XmlEnumValue(value="Midterm")
    MIDTERM,
    @XmlEnumValue(value="Lab")
    LAB,
    @XmlEnumValue(value="Quiz")
    QUIZ
}
