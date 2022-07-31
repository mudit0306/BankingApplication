Feature: Implement logic to Get All Booking IDs with and without Filer for the existing Booking

  @GetAllBookingIDsE2E
  Scenario: Getting All Booking IDs successfully
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingPayload.json|
    And User should be able to retrieve required Response from "GetAllBookingIDs" Endpoint with Successful Status code
    Then validate the records are updated successfully after hitting "GetAllBookingIDs"

  @GetAllBookingIDs-ByFilter-Names
  Scenario:Get All Booking IDs with Filter Names
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingForFiltersPayload.json|
    And User should be able to retrieve required Response from "GetBookingById" Endpoint with Successful Status code
    When Parameters are set with Filter Values
      |firstname|lastname|
      |Jason|Filter Cruise|
    And User should be able to retrieve required Response from "GetAllBookingIDs" Endpoint with Successful Status code
    Then validate the records are updated successfully after hitting "GetAllBookingIDs"


  @GetAllBookingIDs-ByFilter-BookingDates
  Scenario:Get All Booking IDs with Filter Check in and Check out Dates
    Given the booking is Created with valid data
      |PayloadFileName|
      |CreateBookingForFiltersPayload.json|
    And User should be able to retrieve required Response from "GetBookingById" Endpoint with Successful Status code
    When Parameters are set with Filter Values
      |checkin|checkout|
      |2022-07-28|2022-07-31|
    And User should be able to retrieve required Response from "GetAllBookingIDs" Endpoint with Successful Status code
    Then validate the records are updated successfully after hitting "GetAllBookingIDs"
