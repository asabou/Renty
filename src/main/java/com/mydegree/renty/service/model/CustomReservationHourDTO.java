package com.mydegree.renty.service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomReservationHourDTO {
    private Integer reservationHour;
    private Long nrReservations;
}
