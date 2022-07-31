package com.bankingapplication.steps;

import com.bankingapplication.steplibs.BookingStepDefLibs;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.System.load;
import static java.lang.System.out;

public class BookingStepdefs {
    BookingStepDefLibs bookingStepDefLibs = new BookingStepDefLibs();

    @When("Payload or Parameters are set")
    public void payloadOrParametersAreSet(List<Map<String,String>> queryParametersList) throws IOException, ParseException {
            bookingStepDefLibs.setQueryParameters(queryParametersList.get(0));
    }

    @Given("the booking is Created with valid data")
    public void theBookingIsCreatedWithValidData(List<Map<String,String>> validationInputs) {
        bookingStepDefLibs.createBooking(validationInputs);
        BookingStepDefLibs.setBookingId(BookingStepDefLibs.getMyResponse());
    }

    @And("validate the records are updated successfully after hitting {string}")
    public void validateTheRecordsAreUpdatedSuccessfullyAfterHitting(String endpoint) throws IOException {
        if(endpoint.equals("PartialUpdateBooking"))
            bookingStepDefLibs.validateResponse(BookingStepDefLibs.getPartiallyUpdatedPayload(), BookingStepDefLibs.getMyResponse().jsonPath().get("."));
        else
        if(endpoint.equals("GetAllBookingIDs")){
            bookingStepDefLibs.validateAllBookingIDsResponse();
        }
        BookingStepDefLibs.log("All records Received in Response for Endpoint " +endpoint + " are as Expected");
    }

    @Then("User should be able to retrieve required Response from {string} Endpoint with Successful Status code")
    public void userShouldBeAbleToRetrieveAllBookingDetailsFromEndpointWithSuccessfulStatusCode(String function) {
        BookingStepDefLibs.log("Going to Perform "+function);
        switch (function) {
            case "PartialUpdateBooking":
                bookingStepDefLibs.endPointCheckAndSetResponse(BookingStepDefLibs.getPartiallyUpdatedPayload());
                break;
            case "DeleteBooking":
                bookingStepDefLibs.endPointCheckAndSetResponse("Delete");
                break;
            case "GetAllBookingIDs":
                bookingStepDefLibs.endPointCheckAndSetResponseAllBookingIDs();
                break;
            default:
                out.println("GET BOOKING BY ID");
                bookingStepDefLibs.endPointCheckAndSetResponse("Get");
                break;
        }
        BookingStepDefLibs.log("Completed " +function +" Successfully");

    }

    @Then("User must able to receive Error Response with Expected Status Code from {string}")
    public void userMustAbleToReceiveErrorResponseWithExpectedStatusCodeFrom(String endpoint,List<Map<String,String>> validations) {
        bookingStepDefLibs.negativeEndpointCheck(validations,endpoint);
    }

    @When("Parameters are set with Filter Values")
    public void ParametersAreSetWithFilterValues(List<Map<String,String>> queryParametersList) {

        bookingStepDefLibs.setFilterParameters(queryParametersList.get(0));

    }
}
