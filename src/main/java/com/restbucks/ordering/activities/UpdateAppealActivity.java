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
import com.restbucks.ordering.representations.OrderRepresentation;
import com.restbucks.ordering.representations.RestbucksUri;

public class UpdateAppealActivity {
    
    public AppealRepresentation update(Appeal appeal, AppealsUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotSubmitted(appealIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        //ADD STUFF HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        //storedOrder.setStatus(storedOrder.getStatus());
        //storedOrder.calculateCost();

        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.CREATED ||
               AppealRepository.current().get(identifier).getStatus() == AppealStatus.SUBMITTED ||
               AppealRepository.current().get(identifier).getStatus() == AppealStatus.INPROCESS ;               
    }
}
