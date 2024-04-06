package com.qa.api.tests.restful_booker_e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateTokenTest {

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
    public void createApiToken() throws IOException {
        String reqJsonBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        APIResponse apiResponse = apiRequestContext.post("https://restful-booker.herokuapp.com/auth",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(reqJsonBody));

        System.out.println("Request Response Status is: " + apiResponse.status());
        System.out.println("Request Response Status Text is: " + apiResponse.statusText());

        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");

        System.out.println("Request Response Text is: " + apiResponse.text());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode apiResponseJson = objectMapper.readTree(apiResponse.body());
        System.out.println("Request Response in Pretty JSON Format: " + apiResponseJson.toPrettyString());

        String tokenId = apiResponseJson.get("token").asText();
        System.out.println("Token Id is: " + tokenId);

        Assert.assertNotNull(tokenId);

    }


}
