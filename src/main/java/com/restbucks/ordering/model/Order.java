package com.restbucks.ordering.model;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order {
    
    private static final Logger LOG = LoggerFactory.getLogger(Order.class);
    
    private final Location location;
    private final List<Item> items;
    @XmlTransient
    private OrderStatus status = OrderStatus.UNPAID;

    public Order(Location location, List<Item> items) {
        this(location, OrderStatus.UNPAID, items);
        LOG.debug("Executing Order constructor: location = {} and items = {}", location, items);
    }
    

    public Order(Location location, OrderStatus status, List<Item> items) {
        this.location = location;
        this.items = items;
        this.status = status;
        LOG.debug("Executing Order constructor: location = {}, status = {} and items = {}", location, status, items);
        LOG.debug("order = {}", this);
    }

    public Location getLocation() {
        LOG.debug("Executing Order.getLocation");
        LOG.debug("location = {}", location);
        return location;
    }
    
    public List<Item> getItems() {
        LOG.debug("Executing Order.getItems");
        LOG.debug("location = {}", items);
        return items;
    }

    public double calculateCost() {
        LOG.debug("Executing Order.calculateCost");
        double total = 0.0;
        if (items != null) {
            for (Item item : items) {
                if(item != null && item.getDrink() != null) {
                    total += item.getDrink().getPrice();
                }
            }
        }
        return total;
    }

    public void setStatus(OrderStatus status) {
        LOG.debug("Executing Order.setStatus");
        this.status = status;
    }

    public OrderStatus getStatus() {
        LOG.debug("Executing Order.getStatus");
        return status;
    }
    
    @Override
    public String toString() {
        LOG.debug("Executing Order.toString");
        StringBuilder sb = new StringBuilder();
        sb.append("Location: " + location + "\n");
        sb.append("Status: " + status + "\n");
        for(Item i : items) {
            sb.append("Item: " + i.toString()+ "\n");
        }
        return sb.toString();
    }
}