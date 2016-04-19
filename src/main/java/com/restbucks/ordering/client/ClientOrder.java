package com.restbucks.ordering.client;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.restbucks.ordering.model.Item;
import com.restbucks.ordering.model.Location;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.model.OrderStatus;
import com.restbucks.ordering.representations.Representation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "order", namespace = Representation.RESTBUCKS_NAMESPACE)
public class ClientOrder {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientOrder.class);
    
    @XmlElement(name = "item", namespace = Representation.RESTBUCKS_NAMESPACE)
    private List<Item> items;
    @XmlElement(name = "location", namespace = Representation.RESTBUCKS_NAMESPACE)
    private Location location;
    @XmlElement(name = "status", namespace = Representation.RESTBUCKS_NAMESPACE)
    private OrderStatus status;
    
    private ClientOrder(){}
    
    public ClientOrder(Order order) {
        LOG.debug("Executing ClientOrder constructor");
        this.location = order.getLocation();
        this.items = order.getItems();
    }
    
    public Order getOrder() {
        LOG.debug("Executing ClientOrder.getOrder");
        return new Order(location, status, items);
    }
    
    public Location getLocation() {
        LOG.debug("Executing ClientOrder.getLocation");
        return location;
    }
    
    public List<Item> getItems() {
        LOG.debug("Executing ClientOrder.getItems");
        return items;
    }

    @Override
    public String toString() {
        LOG.debug("Executing ClientOrder.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientOrder.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public OrderStatus getStatus() {
        LOG.debug("Executing ClientOrder.getStatus");
        return status;
    }

    public double getCost() {
        LOG.debug("Executing ClientOrder.getCost");
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
}