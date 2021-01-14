package com.mydegree.renty.service.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationInputDTO {
    private Long entertainmentActivityId;
    private Long entertainmentPlaceId;
    private Timestamp reservationDate;
    private Integer reservationHour;
    private Long rentalRepresentativeId;
}
