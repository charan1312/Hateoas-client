package com.hateoas.appeals.activities;

import com.restbucks.ordering.activities.NoSuchOrderException;
import com.restbucks.ordering.activities.UpdateException;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.model.OrderStatus;
import com.restbucks.ordering.repositories.OrderRepository;
import com.restbucks.ordering.representations.OrderRepresentation;
import com.restbucks.ordering.representations.RestbucksUri;

public class UpdateOrderActivity {
    
    public OrderRepresentation update(Order order, RestbucksUri orderUri) {
        Identifier orderIdentifier = orderUri.getId();

        OrderRepository repository = OrderRepository.current();
        if (OrderRepository.current().orderNotPlaced(orderIdentifier)) { // Defensive check to see if we have the order
            throw new NoSuchOrderException();
        }

        if (!orderCanBeChanged(orderIdentifier)) {
            throw new UpdateException();
        }

        Order storedOrder = repository.get(orderIdentifier);
        
        storedOrder.setStatus(storedOrder.getStatus());
        storedOrder.calculateCost();

        return OrderRepresentation.createResponseOrderRepresentation(storedOrder, orderUri); 
    }
    
    private boolean orderCanBeChanged(Identifier identifier) {
        return OrderRepository.current().get(identifier).getStatus() == OrderStatus.UNPAID;
    }
}
