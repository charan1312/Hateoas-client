package com.hateoas.appeals.activities;

import com.restbucks.ordering.activities.NoSuchOrderException;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.repositories.OrderRepository;
import com.restbucks.ordering.representations.OrderRepresentation;
import com.restbucks.ordering.representations.RestbucksUri;

public class ReadOrderActivity {
    public OrderRepresentation retrieveByUri(RestbucksUri orderUri) {
        Identifier identifier  = orderUri.getId();
        
        Order order = OrderRepository.current().get(identifier);
        
        if(order == null) {
            throw new NoSuchOrderException();
        }
        
        return OrderRepresentation.createResponseOrderRepresentation(order, orderUri);
    }
}
