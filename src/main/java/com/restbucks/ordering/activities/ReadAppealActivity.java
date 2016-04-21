package com.restbucks.ordering.activities;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.repositories.AppealRepository;
import com.restbucks.ordering.representations.AppealRepresentation;
import com.restbucks.ordering.representations.AppealsUri;

public class ReadAppealActivity {
    public AppealRepresentation retrieveByUri(AppealsUri appealUri) {
        Identifier identifier  = appealUri.getId();
        
        Appeal appeal = AppealRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new NoSuchAppealException();
        }
        
        return AppealRepresentation.createResponseAppealRepresentation(appeal, appealUri);
    }
}
