Feature: Implement logic to Partially update the existing Booking using Booking Id

  @PartialUpdateBookingE2E
  Scenario:Partial update a Booking Successfully
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    When Payload or Parameters are set
      |firstname|totalprice|additionalneeds|checkin|
      |James|500|Dinner|2022-02-02|
    Then User should be able to retrieve required Response from "PartialUpdateBooking" Endpoint with Successful Status code
    And User should be able to retrieve required Response from "GetBookingById" Endpoint with Successful Status code
    Then validate the records are updated successfully after hitting "PartialUpdateBooking"

  @PartialUpdateBooking-BlankValues
  Scenario:Partial update a Booking Successfully with few Blank values
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    When Payload or Parameters are set
      |lastname|totalprice|additionalneeds|checkin|
      ||200||2022-02-02|
    Then User should be able to retrieve required Response from "PartialUpdateBooking" Endpoint with Successful Status code
    And User should be able to retrieve required Response from "GetBookingById" Endpoint with Successful Status code
    Then validate the records are updated successfully after hitting "PartialUpdateBooking"

  @PartialUpdateBooking-InvalidDate
  Scenario: Negative Scenario for Invalid Date in Partial update Request
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    When Payload or Parameters are set
      |lastname|totalprice|additionalneeds|checkin|
      |James|800|Dinner|2022-22-02|
    Then User must able to receive Error Response with Expected Status Code from "PartialUpdateBooking"
      |ErrorMessage|StatusCode|
      |Invalid date|200|


