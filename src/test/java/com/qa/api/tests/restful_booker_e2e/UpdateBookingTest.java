package com.qa.api.tests.restful_booker_e2e;

import com.api.data.Booking;
import com.api.data.BookingDate;
import com.api.data.BookingResponse;
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

public class UpdateBookingTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext apiRequestContext;

    private static Integer bookingId;
    private static String tokenId;

    @BeforeTest
    public void setup() throws IOException {
        playwright = Playwright.create();
        apiRequest = playwright.request();
        apiRequestContext = apiRequest.newContext();
        String reqJsonBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        APIResponse apiResponse = apiRequestContext.post("https://restful-booker.herokuapp.com/auth",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(reqJsonBody));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode apiResponseJson = objectMapper.readTree(apiResponse.body());
        tokenId = apiResponseJson.get("token").asText();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    Booking booking = Booking.builder()
            .firstname("Amily")
            .lastname("Jonas")
            .totalprice(10000.99)
            .depositpaid(true)
            .bookingdates(BookingDate.builder().checkin("2023-09-10").checkout("2023-09-14").build())
            .additionalneeds("Breakfast")
            .build();

    @Test(priority = 1)
    public void createBooking() throws IOException {
        APIResponse apiResponse = apiRequestContext.post("https://restful-booker.herokuapp.com/booking",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(booking));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());

        String postApiResponse = apiResponse.text();
        System.out.println("The POST response is: " + postApiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        BookingResponse apiJsonResponse = objectMapper.readValue(apiResponse.body(), BookingResponse.class);

        bookingId = apiJsonResponse.getBookingid();
        Assert.assertNotNull(bookingId);
        System.out.println("The Booking Id is: " + bookingId);
    }


    @Test(priority = 2)
    public void updateBooking() {
        booking.setAdditionalneeds("Breakfast & Dinner");
        booking.setDepositpaid(false);
        booking.setLastname("Jones");
        APIResponse apiResponse = apiRequestContext.put("https://restful-booker.herokuapp.com/booking/" + bookingId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setHeader("Cookie", "token=" + tokenId)
                        .setData(booking));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());

        String postApiResponse = apiResponse.text();
        System.out.println("The PUT response is: " + postApiResponse);

    }


    @Test(priority = 3)
    public void updatePartialBooking() throws IOException {
        APIResponse apiResponse = apiRequestContext.patch("https://restful-booker.herokuapp.com/booking/" + bookingId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setHeader("Cookie", "token=" + tokenId)
                        .setData("{\n" +
                                "    \"firstname\" : \"James\",\n" +
                                "    \"lastname\" : \"Brown\"\n" +
                                "}"));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());

        String postApiResponse = apiResponse.text();
        System.out.println("The PATCH response is: " + postApiResponse);

        //GET CALL by booking id to check the updates

        APIResponse apiGetResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking/" + bookingId);
        Assert.assertEquals(apiGetResponse.status(), 200);
        Assert.assertEquals(apiGetResponse.statusText(), "OK");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode apiJSONResponse = objectMapper.readTree(apiGetResponse.body());
        System.out.println("The pretty Json response is: " + apiJSONResponse.toPrettyString());
        Booking actualBookingResponse = objectMapper.readValue(apiGetResponse.body(), Booking.class);

        Assert.assertEquals(actualBookingResponse.getFirstname(), "James");
        Assert.assertEquals(actualBookingResponse.getLastname(), "Brown");
        Assert.assertEquals(actualBookingResponse.getDepositpaid(), booking.getDepositpaid());
        Assert.assertEquals(actualBookingResponse.getAdditionalneeds(), booking.getAdditionalneeds());
    }

    @Test(priority = 4)
    public void deleteBooking() {
        APIResponse apiResponse = apiRequestContext.delete("https://restful-booker.herokuapp.com/booking/" + bookingId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Cookie", "token=" + tokenId));
        Assert.assertEquals(apiResponse.status(), 201);
        Assert.assertEquals(apiResponse.statusText(), "Created");
        Assert.assertTrue(apiResponse.ok());
        System.out.println("The DELETE endpoint is: " + apiResponse.url());

    }

    @Test(priority = 5)
    public void getBookingById() {
        APIResponse apiResponse = apiRequestContext.get("https://restful-booker.herokuapp.com/booking/" + bookingId);
        Assert.assertEquals(apiResponse.status(), 404);
        Assert.assertEquals(apiResponse.statusText(), "Not Found");
        System.out.println("The GET endpoint is: " + apiResponse.url());
    }


}
