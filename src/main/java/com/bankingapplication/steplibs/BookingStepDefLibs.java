package com.bankingapplication.steplibs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static java.lang.System.*;
import static java.lang.System.out;
import static net.serenitybdd.rest.SerenityRest.given;

public class BookingStepDefLibs {
    private File jsonData;
    private static final String TOKEN = "YWRtaW46cGFzc3dvcmQxMjM=";
    public static Map<String, Object> getPartiallyUpdatedPayload() {
        return partiallyUpdatedPayload;
    }

    public static Map<String, Object> partiallyUpdatedPayload ;

    public static void setBookingforAllIDs(Response myResponse) {
        BookingStepDefLibs.myResponse = myResponse;
    }

    public static Response getMyResponse() {
        return myResponse;
    }

    public static void setMyResponse(Response myResponse) {
        BookingStepDefLibs.myResponse = myResponse;
    }


    private static Response myResponse = null;

    public static String getBookingId() {
        return bookingId;
    }

    private static String bookingId="";

    public void setPayload(String fileName) {
        setJsonData(new File("src/main/resources/payloads/"+fileName));
    }

    public File getPayload(){
        return jsonData;
    }

    public static Map<String, String> getFilterParams() {
        return filterParams;
    }

    static Map<String,String> filterParams = new HashMap<>();

    @Step
    public void endPointCheckAndSetResponse(File payload) {
        log("Going to perform CREATE BOOKING");
            Response response =
                    given()
                        .body(payload)
                    // WHEN
                    .when()
                        .post()
                    // THEN
                        .then().extract().response();
        validateStatusCode(response);
        setMyResponse(response);
    }

    @Step
    public void endPointCheckAndSetResponse(String endpoint) {
        Response response = null;
        if(endpoint.equals("Get"))
        {
        response = given()
                .get("/"+getBookingId())
                // THEN
                .then().extract().response();
        }
        else if(endpoint.equals("Delete"))
        {
            response = given()
                    .header("Authorization", "Basic "+TOKEN)
                    .when()
                    .delete("/"+getBookingId())
                    // THEN
                    .then().extract().response();
        }

        assert response != null;
        validateStatusCode(response);
        setMyResponse(response);

    }
    @Step
    public void endPointCheckAndSetResponse(Map<String, Object> payload) {
        Response response = given()
                .header("Authorization", "Basic "+TOKEN)
                .body(payload)
                // WHEN
                .when()
                .put("/"+getBookingId())
                // THEN
                .then().extract().response();
        validateStatusCode(response);
        setMyResponse(response);

    }

    public void endPointCheckAndSetResponseAllBookingIDs() {
        Map<String, String> filterParameters = getFilterParams();
        Response response;
            response = given()
                .queryParams(filterParameters)
                .when()
                .get()
                // THEN
                .then().extract().response();

        validateStatusCode(response);
        setBookingforAllIDs(response);
    }
    public void validateStatusCode(Response response){
            out.println("status code is: " + response.statusCode());
            Assert.assertTrue("Success Status Code Not Received- S"+response.statusCode(),response.statusCode() == 200 || response.statusCode() == 201);
    }

    public static void setBookingId(Response myResponse) {
        JsonPath json = myResponse.jsonPath();
        bookingId=json.get("bookingid").toString();
        log("Booking ID Generated - " + bookingId);

    }

    public void setJsonData(File jsonData) {
        this.jsonData = jsonData;
    }

    @Step
    public void setQueryParameters(Map<String, String> stringStringMap) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(FileUtils.readFileToString(getPayload(),"UTF-8"));
        //Reading the String
        for(Map.Entry<String,String> entry: stringStringMap.entrySet()) {
            if(entry.getKey().equals("checkin")||entry.getKey().equals("checkout")){
                JSONObject bookingDateMap= (JSONObject) jsonObject.get("bookingdates");
                bookingDateMap.put(entry.getKey(),entry.getValue());
            }
            else if(entry.getKey().equals("totalprice") && entry.getValue()!=null) {
                long newVal;
                BigDecimal bigDecimal = new BigDecimal(entry.getValue());
                bigDecimal.setScale(0, BigDecimal.ROUND_HALF_DOWN);
                newVal = bigDecimal.longValue();
                jsonObject.put(entry.getKey(), newVal);
            }
            else
            jsonObject.put(entry.getKey(), entry.getValue());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> payloadMap = objectMapper.readValue(jsonObject.toString(),HashMap.class);
        replaceNullValueInMap(payloadMap);
        setPartiallyUpdatedPayload(payloadMap);

    }

    private void replaceNullValueInMap(Map<String, Object> payloadMap) {
        payloadMap.entrySet()
                .forEach(entry -> {
                    if (entry.getValue() == null)
                        entry.setValue("");
                });
    }

    private static void setPartiallyUpdatedPayload(Map<String, Object> jsonObject) {
        BookingStepDefLibs.partiallyUpdatedPayload=jsonObject;
    }

    @Step
    public void createBooking(List<Map<String,String>> validations) {
        String payloadFileName =  validations.get(0).get("PayloadFileName");
        setPayload(payloadFileName);
        endPointCheckAndSetResponse(getPayload());
        log("Completed CREATE BOOKING" );

        }

    @Step
    public void validateResponse(Map<String, Object> expectedMap,Map<String, Object> actualMap) throws IOException {
        out.println("in validation"+actualMap);
//        Map<String,Object> actualResponse = payload.jsonPath().get(".");

        Assert.assertEquals("Size of Payload Response sent and Partial Response received don't match",
                actualMap.size(), expectedMap.size());

        Assert.assertEquals("Response is not expected",
                actualMap, expectedMap);
    }

    public void negativeEndpointCheck(List<Map<String, String>> validations, String endpoint) {
        String expectedErrorMessage = validations.get(0).get("ErrorMessage");
        String expectedSatusCode = validations.get(0).get("StatusCode");
        Response response = null;
        if(endpoint.equals("PartialUpdateBooking")) {
            response = given()
                    .header("Authorization", "Basic "+TOKEN)
                    .body(BookingStepDefLibs.getPartiallyUpdatedPayload())
                    // WHEN
                    .when()
                    .put("/"+getBookingId())
                    // THEN
                    .then().extract().response();

        }else if(endpoint.equals("GetBookingById")){
            response = given()
                    .get("/"+getBookingId())
                    // THEN
                    .then().extract().response();
        }
        assert response != null;
        Assert.assertEquals(expectedErrorMessage,response.asString());
        Assert.assertEquals(expectedSatusCode,String.valueOf(response.statusCode()));

    }


    public void validateAllBookingIDsResponse() {
        List<Integer> actualResponse = getMyResponse().jsonPath().get("bookingid");
        log("Total Booking IDs Returned in Response " +actualResponse.size());

        // Validate booking created is present in Get All booking IDs Response
        Assert.assertTrue("Booking Created is not present in Bookind ID List Response",
                actualResponse.contains(Integer.parseInt(getBookingId())));
        log("Booking ID- " +getBookingId() +" Is present in AllBookingIDs Response");
    }


    public void setFilterParameters(Map<String, String> queryParams) {
        for(Map.Entry<String, String> params : queryParams.entrySet()){
            filterParams.put(params.getKey(),params.getValue());
        }
    }

    // Print Description to Serenity reports
    public static void log(String description) {
        StepEventBus.getEventBus().stepStarted
                (ExecutedStepDescription.withTitle(description));
        StepEventBus.getEventBus().stepFinished();
        out.println(description);
    }

}
