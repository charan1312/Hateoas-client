package com.restbucks.ordering.activities;

import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.repositories.GradeRepository;
import com.restbucks.ordering.representations.AppealsUri;
import com.restbucks.ordering.representations.GradeRepresentation;
import com.restbucks.ordering.representations.Link1;
import com.restbucks.ordering.representations.Representation1;

public class ReadGradeActivity {
    public GradeRepresentation retrieveByUri(AppealsUri gradeUri) {
        int identifier  = gradeUri.getGradeId();
        
        Grade grade = GradeRepository.current().get(identifier);
        
        if(grade == null) {
            throw new NoSuchGradeException();
        }
        
        return new GradeRepresentation(grade, 
                new Link1(Representation1.RELATIONS_URI + "grade", gradeUri));
    }
}