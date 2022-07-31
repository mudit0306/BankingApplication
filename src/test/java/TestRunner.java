import io.cucumber.junit.CucumberOptions;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)

@CucumberOptions(
        features= {"src/main/resources/features/PartialUpdateBooking.feature",
                "src/main/resources/features/GetAllBookingIDs.feature",
                "src/main/resources/features/DeleteBooking.feature"}
//        tags = "@GetAllBookingIDsE2E"

)

public class TestRunner {

    @BeforeClass
    public static void setupBaseURL(){
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com/booking").addHeader("Content-Type",
                        "application/json")
                .addHeader("Accept","application/json")
                .addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter())
                .build();

    }
}
