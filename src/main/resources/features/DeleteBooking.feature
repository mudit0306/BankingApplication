Feature: Implement logic to Delete the existing Booking using Booking Id

  @DeleteBookingE2E
  Scenario:Delete an Existing Booking Successfully
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    Then User should be able to retrieve required Response from "DeleteBooking" Endpoint with Successful Status code
    Then User must able to receive Error Response with Expected Status Code from "GetBookingById"
      |ErrorMessage|StatusCode|
      |Not Found|404|

  @DeletePartiallyUpdatedBooking
  Scenario:Delete a Partially updated Booking Successfully
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    When Payload or Parameters are set
      |firstname|totalprice|additionalneeds|checkin|
      |Dwayne|112|Lunch|2022-02-01|
    Then User should be able to retrieve required Response from "PartialUpdateBooking" Endpoint with Successful Status code
    And User should be able to retrieve required Response from "GetBookingById" Endpoint with Successful Status code
    Then User should be able to retrieve required Response from "DeleteBooking" Endpoint with Successful Status code
    Then User must able to receive Error Response with Expected Status Code from "GetBookingById"
      |ErrorMessage|StatusCode|
      |Not Found|404|


