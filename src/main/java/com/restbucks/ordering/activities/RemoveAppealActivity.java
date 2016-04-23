package com.restbucks.ordering.activities;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.AppealStatus;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.repositories.AppealRepository;
import com.restbucks.ordering.representations.AppealRepresentation;
import com.restbucks.ordering.representations.AppealsUri;

public class RemoveAppealActivity {
    public AppealRepresentation delete(AppealsUri orderUri) {
        // Discover the URI of the order that has been cancelled
        
        Identifier identifier = orderUri.getId();

        AppealRepository appealRepository = AppealRepository.current();

        if (appealRepository.appealNotSubmitted(identifier)) {
            throw new NoSuchAppealException();
        }

        Appeal appeal = appealRepository.get(identifier);

        // Can't delete an appeal thats in process or updaegrade or approved or rejected
        if (appeal.getStatus() == AppealStatus.INPROCESS || appeal.getStatus() == AppealStatus.APPROVED || appeal.getStatus() == AppealStatus.UPDATEGRADE || appeal.getStatus() == AppealStatus.REJECTED) {
            throw new AppealDeletionException();
        }

        //Can delete an appeal that is in submitted or followup state
        if(appeal.getStatus() == AppealStatus.FOLLOWUP || appeal.getStatus() == AppealStatus.SUBMITTED) { // An unpaid order is being cancelled 
            appealRepository.remove(identifier);
        }
        return new AppealRepresentation(appeal);
    }
}
