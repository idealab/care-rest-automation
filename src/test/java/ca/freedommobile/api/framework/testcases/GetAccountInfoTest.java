package ca.freedommobile.api.framework.testcases;

import ca.freedommobile.api.framework.apis.APIManger;
import ca.freedommobile.api.framework.config.Config;
import ca.freedommobile.api.framework.reports.HTMLReport;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import cucumber.api.CucumberOptions;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import cucumber.api.junit.Cucumber;
import groovy.json.StringEscapeUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import static org.hamcrest.Matchers.*;

/**
 * @project api-framework
 * Created By Rsingh on 2019-05-13.
 */

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:FeaturesFiles")
public class GetAccountInfoTest {
    private ExtentTest extent;
    Response response;

    Logger logger = LogManager.getLogger(GetAccountInfoTest.class);


    @Before
    public void before(Scenario scenario) {
        logger.info("Running Scenario  - {} ", scenario.getName());
        extent = HTMLReport.getInstance().getReports().createTest(scenario.getName());
    }

    /**
     * Scenario 1 Implementation-GET Retrieves the account info with invalid id.
     */

    @Given("user enter the invalid contactId with wrong api path.")
    public void user_enter_the_invalid_contactId_with_wrong_api_path() {

    }

    //@When("User submit the Get request msisdnEncrypted \"([^\"]*)\" as path param$")
    @When("User calls account info with \"([^\"]*)\" as path param")
    public void user_submit_the_Get_request_msisdnEncrypted_as_path_param(String contactId) {
        contactId = StringEscapeUtils.unescapeJava(contactId);
        Map<String, Object> map = Collections.singletonMap("contactId", contactId);
        response = APIManger.getInstance().doGet(Config.getProperty("api.accountinfo-contactid"), map, null);

        extent.log(Status.INFO, "API called with contact ID  " + contactId);
    }

    @Then("status code should be \"([^\"]*)\" in the response")
    public void status_code_should_be_in_the_response(Integer statusCode) {
        logger.info("Status code to be matched from Scenario-  {}", statusCode);
        //System.out.println(response.body().print());
        int code = response.then().extract().statusCode();
        if (statusCode == code) {
            extent.log(Status.PASS, String.format("Response Status code is %d - it matches with the desired code %d", code, statusCode));
        } else {
            extent.log(Status.FAIL, String.format("Response Status code is %d -it does't Matches with desired code %d", code, statusCode));
        }
        response.then().assertThat().statusCode(statusCode);
    }


    @And("response code should be \"([^\"]*)\" in response body")
    public void responseCodeShouldBeInResponseBody(Integer errCode) {
        JsonPath path = response.then().extract().jsonPath();
        String code;
        if (response.then().extract().statusCode() == 500) {
            code = path.get("Messages[0].code");
        } else {
            code = path.get("messages[0].code");
        }
        if (code.equals(errCode)) {
            extent.log(Status.PASS, String.format("Code in response messages- %s  - it matches with desired Code - %s", code, errCode));
        } else {
            extent.log(Status.FAIL, String.format("Code in response messages - %s  -it doesn't matches with desired Code - %s", code, errCode));
        }

        if (response.then().extract().statusCode() == 500) {
            response.then().assertThat().body("Messages[0].code", equalTo(errCode));
        } else {
            response.then().assertThat().body("messages[0].code", equalTo(errCode));
        }
    }


    @And("response type should be \"([^\"]*)\" in response body")
    public void response_type_should_be_in_response_body(String errType) {
        JsonPath path = response.then().extract().jsonPath();

        String type;
        if (response.then().extract().statusCode() == 500) {
            type = path.get("Messages[0].Type");
        } else {
            type = path.get("messages[0].type");
        }

        if (type.equals(errType)) {
            extent.log(Status.PASS, String.format("Type in response - %s  - it matches with desired Type - %s", type, errType));
        } else {
            extent.log(Status.FAIL, String.format("Type in response - %s  -it doesn't matches with desired Type - %s", type, errType));
        }
        if (response.then().extract().statusCode() == 500) {
            response.then().assertThat().body("Messages[0].Type", equalTo(errType));
        } else {
            response.then().assertThat().body("messages[0].type", equalTo(errType));
        }
    }

    @And("Description of response should be \"([^\"]*)\"")
    public void description_of_response_should_be(String desc) {
        JsonPath path = response.then().extract().jsonPath();
        String description = null;
        if (response.then().extract().statusCode() == 500) {
            description = path.get("Messages[0].Description");
        } else {
            description = path.get("messages[0].description");
        }

        if (description.equals(desc)) {
            extent.log(Status.PASS, String.format("Description in reposnse - %s  - it matches with desired Description - %s", description, desc));
        } else {
            extent.log(Status.FAIL, String.format("Description in reposnse - %s  it doesn't matches with desired Description - %s", description, desc));
        }
        if (response.then().extract().statusCode() == 500) {
            response.then().assertThat().body("Messages[0].Description", equalTo(desc));
        } else {
            response.then().assertThat().body("messages[0].description", equalTo(desc));
        }

    }

    /**
     * Scenario 2
     *
     * @throws Throwable
     */

    @Given("^user enter valid the contact ID\\.$")
    public void userEnterValidTheContactID() throws Throwable {

    }

    @And("^response body includes the serviceItemArray$")
    public void responseBodyIncludesTheServiceItemArray() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        JsonPath path = response.then().extract().jsonPath();
        Object serviceItems = path.get("serviceItems");
        if (null != serviceItems) {
            extent.log(Status.PASS, String.format("Service Item Object is present the in the body"));
        }else {
            extent.log(Status.FAIL, String.format("Service Item Object is NOT present the in the body"));
        }
        response.then().body("$",hasKey("serviceItems"));
    }

    @And("^response serviceItemArray should include \"([^\"]*)\"$")
    public void responseShouldIncludeFollowingItems(String items){
        String[] itemsArr = StringUtils.split(items, ",");
        if(itemsArr.length==1){
            extent.log(Status.PASS, String.format("Service Item Object - %s the in the body", itemsArr[0]));
            response.then().assertThat().body("$", hasItem(itemsArr[0]));
        }else
        if(itemsArr.length==2){
            extent.log(Status.PASS, String.format("Service Item Object - %s the in the body", itemsArr[0]));
            extent.log(Status.PASS, String.format("Service Item Object - %s the in the body", itemsArr[1]));
            response.then().assertThat().body("$", hasItem(itemsArr[0]));
            response.then().assertThat().body("$", hasItem(itemsArr[1]));
        }else
        if(itemsArr.length==3){
            extent.log(Status.PASS, String.format("Service Item Object - %s the in the body", itemsArr[0]));
            extent.log(Status.PASS, String.format("Service Item Object - %s the in the body", itemsArr[1]));
            extent.log(Status.PASS, String.format("Service Item Object - %s the in the body", itemsArr[2]));
            response.then().assertThat().body("$", hasItem(itemsArr[0]));
            response.then().assertThat().body("$", hasItem(itemsArr[1]));
            response.then().assertThat().body("$", hasItem(itemsArr[2]));
        }else {
            extent.log(Status.FAIL, String.format("Service Items doesn't match with expected value"));
        }

    }


    @And("^response serviceItem should include userType \"([^\"]*)\"$")
    public void responseServiceItemShouldIncludeUserType(String userType){
        JsonPath path = response.then().extract().jsonPath();
        Object type = path.get("userType");
        if(null==type){
            extent.log(Status.FAIL, String.format("Usertype is not found in the response "));
        }
        else if(type!=userType){
            extent.log(Status.FAIL, String.format("Usertype %s is not found in the response expected - %s - found %s", userType, userType,type ));
        }else {
            extent.log(Status.PASS, String.format("Usertype %s matched in the response expected", userType));
        }
        response.then().assertThat().body("userType", equalTo(userType));
    }


}