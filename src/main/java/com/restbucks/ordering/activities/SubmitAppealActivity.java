package com.restbucks.ordering.activities;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.AppealStatus;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.model.OrderStatus;
import com.restbucks.ordering.repositories.AppealRepository;
import com.restbucks.ordering.repositories.OrderRepository;
import com.restbucks.ordering.representations.AppealRepresentation;
import com.restbucks.ordering.representations.AppealsUri;
import com.restbucks.ordering.representations.Link;
import com.restbucks.ordering.representations.Link1;
import com.restbucks.ordering.representations.OrderRepresentation;
import com.restbucks.ordering.representations.Representation;
import com.restbucks.ordering.representations.Representation1;
import com.restbucks.ordering.representations.RestbucksUri;

public class SubmitAppealActivity {
    public AppealRepresentation create(Appeal appeal, AppealsUri requestUri) {
        appeal.setAppealStatus(AppealStatus.CREATED);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        AppealsUri appealUri = new AppealsUri(requestUri.getBaseUri() + "/appeal/" + identifier.toString());
        AppealsUri gradeUri = new AppealsUri(requestUri.getBaseUri() + "/grade/" + appeal.getGradeId());
        //RestbucksUri paymentUri = new RestbucksUri(requestUri.getBaseUri() + "/payment/" + identifier.toString());
        return new AppealRepresentation(appeal, 
                new Link1(Representation1.RELATIONS_URI + "delete", appealUri),
                new Link1(Representation1.RELATIONS_URI + "process", appealUri),
                new Link1(Representation1.SELF_REL_VALUE, appealUri));
    }
}
