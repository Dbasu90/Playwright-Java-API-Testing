package com.api.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class BookingDate {
    private String checkin;
    private String checkout;
}
