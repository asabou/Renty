package com.mydegree.renty.dao.entity.custom;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomReservationDate {
    private Date reservationDate;
    private Long nrReservations;
}
