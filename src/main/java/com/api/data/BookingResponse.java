package com.api.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString
public class BookingResponse {
    private Integer bookingid;
    private Booking booking;

}
