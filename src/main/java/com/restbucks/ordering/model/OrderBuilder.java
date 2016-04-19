package com.restbucks.ordering.model;

import static com.restbucks.ordering.model.ItemBuilder.item;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderBuilder.class);
    
    public static OrderBuilder order() {
        return new OrderBuilder();
    }

    private Location location = Location.TAKEAWAY;
    private ArrayList<Item> items = null;
    private OrderStatus status = OrderStatus.UNPAID;
    
    private void defaultItems() {
        LOG.debug("Executing OrderBuilder.defaultItems");
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item().build());
        this.items = items;
    }
    
    private void corruptItems() {
        LOG.debug("Executing OrderBuilder.corruptItems");
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(null);
        items.add(null);
        items.add(null);
        items.add(null);
        this.items = items;
    }
   
    
    public Order build() {
        LOG.debug("Executing OrderBuilder.build");
        if(items == null) {
            defaultItems();
        }
        return new Order(location, status, items);
    }

    public OrderBuilder withItem(Item item) {
        LOG.debug("Executing OrderBuilder.withItem");
        if(items == null) {
            items = new ArrayList<Item>();
        }
        items.add(item);
        return this;
    }


    public OrderBuilder withCorruptedValues() {
        LOG.debug("Executing OrderBuilder.withCorruptedValues");
        corruptItems();
        return this;
    }
    
    public OrderBuilder withStatus(OrderStatus status) {
        LOG.debug("Executing OrderBuilder.withRandomItems");
        this.status = status;
        return this;
    }

    public OrderBuilder withRandomItems() {
        LOG.debug("Executing OrderBuilder.withRandomItems");
        int numberOfItems = (int) (System.currentTimeMillis() % 10 + 1);
        this.items = new ArrayList<Item>();
        for(int i = 0; i < numberOfItems; i++) {
            items.add(item().random().build());
        }
        return this;
    }

}
