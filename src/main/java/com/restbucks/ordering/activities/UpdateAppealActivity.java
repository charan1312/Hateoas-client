package com.restbucks.ordering.activities;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.AppealStatus;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.repositories.AppealRepository;
import com.restbucks.ordering.representations.AppealRepresentation;
import com.restbucks.ordering.representations.AppealsUri;
import com.restbucks.ordering.representations.Link1;
import com.restbucks.ordering.representations.Representation1;

public class UpdateAppealActivity {
    
    public AppealRepresentation update(Appeal appeal, AppealsUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealsUri gradeUri = new AppealsUri(appealUri.getBaseUri() + "/grade/" + appeal.getGradeId());
        
        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotSubmitted(appealIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        //Update either the status of the appeal or the comments column of the appeal
        storedAppeal.setAppealStatus(appeal.getStatus());
        storedAppeal.setComments(appeal.getComments());

        if(appeal.getStatus() == AppealStatus.FOLLOWUP) {
            return  new AppealRepresentation(storedAppeal, 
                    new Link1(Representation1.RELATIONS_URI + "process", appealUri),
                    new Link1(Representation1.RELATIONS_URI + "update", appealUri),
                    new Link1(Representation1.RELATIONS_URI + "delete", appealUri),
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.INPROCESS) {
            return  new AppealRepresentation(storedAppeal, 
                    new Link1(Representation1.RELATIONS_URI + "update", appealUri),
                    new Link1(Representation1.RELATIONS_URI + "reject", appealUri), 
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.UPDATEGRADE) {
            return  new AppealRepresentation(storedAppeal,
                    new Link1(Representation1.RELATIONS_URI + "approve", appealUri),
                    new Link1(Representation1.RELATIONS_URI + "grade", gradeUri),
                    new Link1(Representation1.RELATIONS_URI + "update", appealUri),
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.APPROVED) {
            return  new AppealRepresentation(storedAppeal,
                    new Link1(Representation1.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.REJECTED) {
            return  new AppealRepresentation(storedAppeal,
                    new Link1(Representation1.SELF_REL_VALUE, appealUri)); 
        } else if(appeal.getStatus() == AppealStatus.DELETED) {
            return  new AppealRepresentation(storedAppeal);
//                    new Link1(Representation1.SELF_REL_VALUE, appealUri));            
        }
        else
            return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.FOLLOWUP ||
               AppealRepository.current().get(identifier).getStatus() == AppealStatus.SUBMITTED ||
               AppealRepository.current().get(identifier).getStatus() == AppealStatus.UPDATEGRADE ||
               AppealRepository.current().get(identifier).getStatus() == AppealStatus.INPROCESS ;               
    }
}
