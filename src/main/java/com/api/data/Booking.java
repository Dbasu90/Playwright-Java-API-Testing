package com.api.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString
public class Booking {
    private String firstname;
    private String lastname;
    private Double totalprice;
    private Boolean depositpaid;
    private BookingDate bookingdates;
    private String additionalneeds;

}
