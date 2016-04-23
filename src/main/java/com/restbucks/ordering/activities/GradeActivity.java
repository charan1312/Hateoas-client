package com.restbucks.ordering.activities;

import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.repositories.GradeRepository;
import com.restbucks.ordering.representations.AppealsUri;
import com.restbucks.ordering.representations.GradeRepresentation;
import com.restbucks.ordering.representations.Link1;
import com.restbucks.ordering.representations.Representation1;

public class GradeActivity {
    public GradeRepresentation updategrade(Grade grade, AppealsUri gradeUri) {
        int identifier = gradeUri.getGradeId();
        
        // Don't know the grade!
        if(!GradeRepository.current().has(identifier)) {
            throw new NoSuchGradeException();
        }
        
        // If the old grade is greater than the new grade,then reject
        if(GradeRepository.current().get(identifier).getGrade() > grade.getGrade()) {
            throw new InvalidGradeException();
        }
        
        // If we get here, let's update the grade 
        GradeRepository.current().store(grade);
        
        return new GradeRepresentation(grade,
                new Link1(Representation1.SELF_REL_VALUE, gradeUri));
    }
}