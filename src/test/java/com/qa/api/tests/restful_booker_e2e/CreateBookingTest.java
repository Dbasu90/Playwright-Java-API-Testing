package com.qa.api.tests.restful_booker_e2e;

import com.api.data.Booking;
import com.api.data.BookingDate;
import com.api.data.BookingResponse;
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

public class CreateBookingTest {
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
    public void createBooking() throws IOException {
        Booking booking = Booking.builder()
                .firstname("Debasmita")
                .lastname("Basu")
                .totalprice(5599.99)
                .depositpaid(true)
                .bookingdates(BookingDate.builder().checkin("2024-04-15").checkout("2024-04-18").build())
                .additionalneeds("Breakfast")
                .build();
        APIResponse apiResponse = apiRequestContext.post("https://restful-booker.herokuapp.com/booking",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(booking));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertEquals(apiResponse.statusText(), "OK");
        Assert.assertTrue(apiResponse.ok());

        String postApiResponse = apiResponse.text();
        System.out.println("The response is: " + postApiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        BookingResponse apiJsonResponse = objectMapper.readValue(apiResponse.body(), BookingResponse.class);

        Assert.assertNotNull(apiJsonResponse.getBookingid());
        System.out.println("The Booking Id is: " + apiJsonResponse.getBookingid());
    }

}
