package com.qa.api.tests.restful_booker_e2e;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GetBookingTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext apiRequestContext;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        apiRequest = playwright.request();
        apiRequestContext = apiRequest.newContext();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    @Test
    public void getBookingAllIds() {
        System.out.println("=======================All IDs==============================");
        APIResponse apiResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking");
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());
        System.out.println("The request response is: " + apiResponse.text());
    }

    @Test
    public void getBookingByName() {
        System.out.println("========================Filter By Name=============================");
        APIResponse apiResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking",
                RequestOptions.create()
                        .setQueryParam("firstname", "Debasmita")
                        .setQueryParam("lastname", "Basu"));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());
        System.out.println("The request response is: " + apiResponse.text());
        Assert.assertFalse(apiResponse.text().isEmpty());
    }

    //this functionality does not work
    @Test
    public void getBookingByDate() {
        System.out.println("=======================Filter By Check-In/Out Dates==============================");
        APIResponse apiResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking",
                RequestOptions.create()
                        .setQueryParam("checkin", "2024-04-15")
                        .setQueryParam("checkout", "2024-04-18"));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());
        System.out.println("The request response is: " + apiResponse.text());
        Assert.assertFalse(apiResponse.text().isEmpty());
    }

}
