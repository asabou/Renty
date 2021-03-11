package com.mydegree.renty.service.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomReservationDateDTO {
    private Date reservationDate;
    private Long nrReservations;
}
