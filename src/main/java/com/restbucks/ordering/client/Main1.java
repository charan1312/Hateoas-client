package com.restbucks.ordering.client;

import static com.restbucks.ordering.model.OrderBuilder.order;

import java.net.URI;
import java.net.URISyntaxException;

import com.restbucks.ordering.client.activities.Actions;
import com.restbucks.ordering.client.activities.GetReceiptActivity;
import com.restbucks.ordering.client.activities.PaymentActivity;
import com.restbucks.ordering.client.activities.PlaceOrderActivity;
import com.restbucks.ordering.client.activities.ReadOrderActivity;
import com.restbucks.ordering.client.activities.UpdateOrderActivity;
import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.model.OrderStatus;
import com.restbucks.ordering.model.Payment;
import com.restbucks.ordering.representations.Link;
import com.restbucks.ordering.representations.OrderRepresentation;
import com.restbucks.ordering.representations.PaymentRepresentation;
import com.restbucks.ordering.representations.ReceiptRepresentation;
import com.restbucks.ordering.representations.RestbucksUri;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main1 {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main1.class);
    
    private static final String APPEALS_MEDIA_TYPE = "application/vnd-cse564-appeals+xml";
    private static final long ONE_MINUTE = 60000; 
    
    private static final String ENTRY_POINT_URI = "http://localhost:8080/CSE564_HATEAOS_RestbucksOrderingServer/webresources/appeal";

/*    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
        happyPathTest(serviceUri);
    }

    private static void hangAround(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void happyPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        // Create the appeal
        LOG.info("Step 1. Create the appeal");
        System.out.println(String.format("About to start happy path test. Creating appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1");
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        LOG.debug("Created orderRepresentation {} denoted by the URI {}", orderRepresentation, orderRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Order placed at [%s]", orderRepresentation.getSelfLink().getUri().toString()));
        
        // Try to update a different order
        LOG.info("\n\nStep 2. Try to update a different order");
        System.out.println(String.format("About to update an order with bad URI [%s] via POST", orderRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        order = order().withRandomItems().build();
        LOG.debug("Created base order {}", order);
        Link badLink = new Link("bad", new RestbucksUri(orderRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), RESTBUCKS_MEDIA_TYPE);
        LOG.debug("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new OrderRepresentation(order));
        LOG.debug("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update order with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));
        
        // Change the order
        LOG.debug("\n\nStep 3. Change the order");
        System.out.println(String.format("About to update order at [%s] via POST", orderRepresentation.getUpdateLink().getUri().toString()));
        order = order().withRandomItems().build();
        LOG.debug("Created base order {}", order);
        Link updateLink = orderRepresentation.getUpdateLink();
        LOG.debug("Created order update link {}", updateLink);
        OrderRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(OrderRepresentation.class, new OrderRepresentation(order));
        LOG.debug("Created updated order representation link {}", updatedRepresentation);
        System.out.println(String.format("Order updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
        
        // Pay for the order 
        LOG.debug("\n\nStep 4. Pay for the order");
        System.out.println(String.format("About to create a payment resource at [%s] via PUT", updatedRepresentation.getPaymentLink().getUri().toString()));
        Link paymentLink = updatedRepresentation.getPaymentLink();
        LOG.debug("Created payment link {} for updated order representation {}", paymentLink, updatedRepresentation);
        LOG.debug("paymentLink.getRelValue() = {}", paymentLink.getRelValue());
        LOG.debug("paymentLink.getUri() = {}", paymentLink.getUri());
        LOG.debug("paymentLink.getMediaType() = {}", paymentLink.getMediaType());
        Payment payment = new Payment(updatedRepresentation.getCost(), "A.N. Other", "12345677878", 12, 2999);
        LOG.debug("Created new payment object {}", payment);
        PaymentRepresentation  paymentRepresentation = client.resource(paymentLink.getUri()).accept(paymentLink.getMediaType()).type(paymentLink.getMediaType()).put(PaymentRepresentation.class, new PaymentRepresentation(payment));        
        LOG.debug("Created new payment representation {}", paymentRepresentation);
        System.out.println(String.format("Payment made, receipt at [%s]", paymentRepresentation.getReceiptLink().getUri().toString()));
        
        // Get a receipt
        LOG.debug("\n\nStep 5. Get a receipt");
        System.out.println(String.format("About to request a receipt from [%s] via GET", paymentRepresentation.getReceiptLink().getUri().toString()));
        Link receiptLink = paymentRepresentation.getReceiptLink();
        LOG.debug("Retrieved the receipt link {} for payment represntation {}", receiptLink, paymentRepresentation);
        ReceiptRepresentation receiptRepresentation = client.resource(receiptLink.getUri()).get(ReceiptRepresentation.class);
        System.out.println(String.format("Payment made, amount in receipt [%f]", receiptRepresentation.getAmountPaid()));
        
        // Check on the order status
        LOG.debug("\n\nStep 6. Check on the order status");
        System.out.println(String.format("About to check order status at [%s] via GET", receiptRepresentation.getOrderLink().getUri().toString()));
        Link orderLink = receiptRepresentation.getOrderLink();
        OrderRepresentation finalOrderRepresentation = client.resource(orderLink.getUri()).accept(RESTBUCKS_MEDIA_TYPE).get(OrderRepresentation.class);
        System.out.println(String.format("Final order placed, current status [%s]", finalOrderRepresentation.getStatus()));
        
        // Allow the barista some time to make the order
        LOG.debug("\n\nStep 7. Allow the barista some time to make the order");
        System.out.println("Pausing the client, press a key to continue");
        System.in.read();
        
        // Take the order if possible
        LOG.debug("\n\nStep 8. Take the order if possible");
        System.out.println(String.format("Trying to take the ready order from [%s] via DELETE. Note: the internal state machine must progress the order to ready before this should work, otherwise expect a 405 response.", receiptRepresentation.getOrderLink().getUri().toString()));
        ClientResponse finalResponse = client.resource(orderLink.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to take final order, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Order status [%s], enjoy your drink", finalResponse.getEntity(OrderRepresentation.class).getStatus()));
        }
    }
    
*/
}
