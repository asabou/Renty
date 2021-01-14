package com.mydegree.renty.service.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationOutputDTO {
    private Long id;
    private String entertainmentPlaceName;
    private String entertainmentActivityName;
    private Timestamp reservationDate;
    private Integer reservationHour;
}
