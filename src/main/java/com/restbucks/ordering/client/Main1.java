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
import com.restbucks.ordering.model.AppealStatus;
import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.model.GradeItem;
import com.restbucks.ordering.model.Order;
import com.restbucks.ordering.model.OrderStatus;
import com.restbucks.ordering.model.Payment;
import com.restbucks.ordering.representations.AppealRepresentation;
import com.restbucks.ordering.representations.AppealsUri;
import com.restbucks.ordering.representations.GradeRepresentation;
import com.restbucks.ordering.representations.Link;
import com.restbucks.ordering.representations.Link1;
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

    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
//        happyPathTest(serviceUri);
//        abandonedPathTest(serviceUri);
//        followUpPathTest(serviceUri);
//        badURIPathTest(serviceUri);
        badIdPathTest(serviceUri);
    }

    private static void badIdPathTest(URI serviceUri) {
        // TODO Auto-generated method stub
        LOG.info("Starting Follow-Up Path Test with Rejection scenario with Service URI {}", serviceUri);
        // Create the appeal
        LOG.info("Step 1. Create the appeal---");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1");
        LOG.debug("Created base appeal--- {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);    //Retrieving the Update Appeal link
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");
        
        //appealRepresentation.getAppeal();
        
        LOG.info("Step 2. Professor Not responding and Student is Following up by changing the state to FOLLOWUP and adding some comments");
        System.out.println(String.format("About to update an appeal with URI [%s] via POST", appealRepresentation.getFollowUpLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.FOLLOWUP);
        LOG.info("Getting the appeal from the previous representation and updating the status to followup");
        appeal = appealRepresentation.getAppeal();     //+
        appeal.setAppealStatus(AppealStatus.FOLLOWUP);     //+
        appeal.addComment("Follow up for the appeal");            
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 badfollowUpLink = new Link1("badid", new AppealsUri(appealRepresentation.getFollowUpLink().getUri().toString() + "-wrong-id"),APPEALS_MEDIA_TYPE);
        LOG.debug("Created followup badID link {}", badfollowUpLink);

        ClientResponse badfollowUpResponse = client.resource(badfollowUpLink.getUri()).accept(badfollowUpLink.getMediaType()).type(badfollowUpLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.info("Created BadID for Followup Response {}", badfollowUpResponse);
        System.out.println(String.format("Tried to update the Appeal with BadID at [%s] via POST, output is [%d]",badfollowUpLink.getUri().toString(),badfollowUpResponse.getStatus()));
        System.out.println("\n\n");
    }

    private static void badURIPathTest(URI serviceUri) {
        // TODO Auto-generated method stub
        LOG.info("Starting Bad URI Path Test with Service URI {}", serviceUri);
        LOG.info("Step 1. Create the appeal--");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1");
        LOG.debug("Created base appeal-- {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);    //Retrieving the Update Appeal link
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");
        
        LOG.info("Step 2. Professor Accepting the appeal and changing the state to INPROCESS with a bad-uri");
        System.out.println(String.format("About to update an appeal with BAD-URI [%s] via POST", appealRepresentation.getProcessLink().getUri().toString() + "/bad-uri"));
        LOG.info("Getting the appeal from the previous representation and updating the status to Inprocess");
        appeal = appealRepresentation.getAppeal();
        appeal.setAppealStatus(AppealStatus.INPROCESS);
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 badprocessLink = new Link1("bad", new AppealsUri(appealRepresentation.getProcessLink().getUri().toString() + "/bad-uri"),APPEALS_MEDIA_TYPE);
        LOG.debug("Created BAD-URI appeal process link {}",  badprocessLink);
        ClientResponse badprocessResponse = client.resource(badprocessLink.getUri()).accept(badprocessLink.getMediaType()).type(badprocessLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.info("Created BAD-URI Response {}", badprocessResponse);
        System.out.println(String.format("Tried to update the Appeal with BAD_URI at [%s] via POST, output is [%d]",badprocessLink.getUri().toString(),badprocessResponse.getStatus()));
        System.out.println("\n\n");
    }

    private static void followUpPathTest(URI serviceUri) {
        // TODO Auto-generated method stub
        LOG.info("Starting Follow-Up Path Test with Rejection scenario with Service URI {}", serviceUri);
        // Create the appeal
        LOG.info("Step 1. Create the appeal---");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1");
        LOG.debug("Created base appeal--- {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);    //Retrieving the Update Appeal link
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");
        
        //appealRepresentation.getAppeal();
        
        LOG.info("Step 2. Professor Not responding and Student is Following up by changing the state to FOLLOWUP and adding some comments");
        System.out.println(String.format("About to update an appeal with URI [%s] via POST", appealRepresentation.getFollowUpLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.FOLLOWUP);
        LOG.info("Getting the appeal from the previous representation and updating the status to followup");
        appeal = appealRepresentation.getAppeal();     //+
        appeal.setAppealStatus(AppealStatus.FOLLOWUP);     //+
        appeal.addComment("Follow up for the appeal");            
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 followUpLink = appealRepresentation.getFollowUpLink();
        LOG.debug("Created appeal followup link {}", followUpLink);
        AppealRepresentation followUpRepresentation = client.resource(followUpLink.getUri()).accept(followUpLink.getMediaType()).type(followUpLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created followup appeal representation link {}", followUpRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", followUpRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");     
        
        LOG.info("Step 3. Professor Accepting the appeal and changing the state to INPROCESS");
        System.out.println(String.format("About to update an appeal with URI [%s] via POST", followUpRepresentation.getProcessLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.INPROCESS);
        LOG.info("Getting the appeal from the previous representation and updating the status to inprocess");
        appeal = followUpRepresentation.getAppeal();      //+
        appeal.setAppealStatus(AppealStatus.INPROCESS);     //+
        appeal.addComment("Looking at the appeal now");     
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 processLink = followUpRepresentation.getProcessLink();
        LOG.debug("Created appeal process link {}", processLink);
        AppealRepresentation inprocessRepresentation = client.resource(processLink.getUri()).accept(processLink.getMediaType()).type(processLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created in-process appeal representation link {}", inprocessRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", inprocessRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");

        LOG.info("Step 4. Professor Rejected the Appeal");
        System.out.println(String.format("About to reject an appeal with URI [%s] via POST", inprocessRepresentation.getRejectLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.REJECTED);
        LOG.info("Getting the appeal from the previous representation and updating the status to Rejected");
        appeal = inprocessRepresentation.getAppeal();  //+
        appeal.setAppealStatus(AppealStatus.REJECTED);     //+
        appeal.addComment("Rejecting the appeal as I dont see it valid");
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 rejectLink = inprocessRepresentation.getRejectLink();
        LOG.debug("Created appeal reject grade link {}", rejectLink);
        AppealRepresentation rejectRepresentation = client.resource(rejectLink.getUri()).accept(rejectLink.getMediaType()).type(rejectLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created reject appeal representation link {}", rejectRepresentation);
        System.out.println(String.format("Appeal rejected at [%s]", rejectRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");
    }

    private static void abandonedPathTest(URI serviceUri) {
        // TODO Auto-generated method stub
        LOG.info("Starting Abandoned Path Test with Service URI {}", serviceUri);
        // Create the appeal
        LOG.info("Step 1. Create the appeal first--");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1");
        LOG.debug("Created base appeal-- {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);    //Retrieving the Update Appeal link
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");

        LOG.info("Step 2. Delete the appeal , meaning abandoning the appeal");
        System.out.println(String.format("Deleting appeal at [%s] via DELETE", appealRepresentation.getDeleteLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.DELETED);
        //LOG.debug("Changed appeal status to deleted before deleting-- {}", appeal);
        LOG.info("Getting the delete appeal link from the previous representation");
        Link1 deleteLink = appealRepresentation.getDeleteLink();
        LOG.debug("Created appeal delete link {}", deleteLink);
        System.out.println("Deleting the appeal");
//        AppealRepresentation deletedRepresentation = client.resource(deleteLink.getUri())
//                .accept(deleteLink.getMediaType())
//                .type(deleteLink.getMediaType())
//                .delete(AppealRepresentation.class);
//        System.out.println("Appeal deleted...");
//        LOG.info(" Deleted appeal representation : {}", deletedRepresentation);
//        System.out.println("\n\n");
        ClientResponse deleteResponse = client.resource(deleteLink.getUri()).accept(deleteLink.getMediaType()).type(deleteLink.getMediaType()).delete(ClientResponse.class);
        LOG.info("Created BAD-URI Response {}", deleteResponse);
        System.out.println(String.format("Deleted the Appeal with URI at [%s] via DELETE, output is [%d]",deleteLink.getUri().toString(),deleteResponse.getStatus()));
        System.out.println("\n\n");

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
        LOG.info("Step 1. Create the appeal--");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1");
        LOG.debug("Created base appeal-- {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);    //Retrieving the Update Appeal link
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal created at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");
        
        LOG.info("Step 2. Professor Accepting the appeal and changing the state to INPROCESS");
        System.out.println(String.format("About to update an appeal with URI [%s] via POST", appealRepresentation.getProcessLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.INPROCESS);
        LOG.info("Getting the appeal from the previous representation and updating the status to Inprocess");
        appeal = appealRepresentation.getAppeal();
        appeal.setAppealStatus(AppealStatus.INPROCESS);
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 processLink = appealRepresentation.getProcessLink();
        LOG.debug("Created appeal process link {}", processLink);
        AppealRepresentation inprocessRepresentation = client.resource(processLink.getUri()).accept(processLink.getMediaType()).type(processLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created in-process appeal representation link {}", inprocessRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", inprocessRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");

        LOG.info("Step 3. Professor Update the appeal status to UPDATEGRADE after which he can access the grade link to update the score");
        System.out.println(String.format("About to update an appeal with URI [%s] via POST", inprocessRepresentation.getUpdateLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.UPDATEGRADE);
        LOG.info("Getting the appeal from the previous representation and updating the status to UpdateGrade");
        appeal = inprocessRepresentation.getAppeal();
        appeal.setAppealStatus(AppealStatus.UPDATEGRADE);
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 updateLink = inprocessRepresentation.getUpdateLink();
        LOG.debug("Created appeal update grade link {}", updateLink);
        AppealRepresentation updateGradeRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created update-grade appeal representation link {}", updateGradeRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", updateGradeRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");

        LOG.info("Step 4.A. Professor Updating the Grade resource using the link from previous step");
        System.out.println(String.format("About to update the grade with URI [%s] via POST", updateGradeRepresentation.getGradeLink().getUri().toString()));
        LOG.info("Creating the updated grade -- ");
        Grade grade = new Grade(1,1,90,"Marks Updated",GradeItem.ASSIGNMENT); 
        LOG.debug("Created updated grade-- {}", grade);
        Link1 gradeLink = updateGradeRepresentation.getGradeLink();
        LOG.debug("Created grade update link {}", gradeLink);
        GradeRepresentation gradeRepresentation = client.resource(gradeLink.getUri()).accept(gradeLink.getMediaType()).type(gradeLink.getMediaType()).put(GradeRepresentation.class, new GradeRepresentation(grade));
        LOG.info("Created updated grade representation link {}", gradeRepresentation);
        System.out.println(String.format("Grade updated at [%s]", gradeRepresentation.getUpdatedGradeLink().getUri().toString()));
        System.out.println("\n\n");

        LOG.info("Step 4.B. Professor Updates the appeal status to APPROVED after updating the grade");
        System.out.println(String.format("About to update an appeal with URI [%s] via POST", updateGradeRepresentation.getApproveLink().getUri().toString()));
        //appeal = new Appeal(1, 1, "Re-Evaluation of my Assignment1",AppealStatus.APPROVED);
        LOG.info("Getting the appeal from the previous representation and updating the status to Approved");
        appeal = updateGradeRepresentation.getAppeal();
        appeal.setAppealStatus(AppealStatus.APPROVED);
        LOG.debug("Created updated appeal-- {}", appeal);
        Link1 approveLink = updateGradeRepresentation.getApproveLink();
        LOG.debug("Created appeal approve link {}", approveLink);
        AppealRepresentation approveRepresentation = client.resource(approveLink.getUri()).accept(approveLink.getMediaType()).type(approveLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created approved appeal representation link {}", approveRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", approveRepresentation.getSelfLink().getUri().toString()));
        System.out.println("\n\n");

/*        // Try to update a different order
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
*/
    }
    

}
