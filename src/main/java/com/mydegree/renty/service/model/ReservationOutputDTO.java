package com.mydegree.renty.service.model;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationOutputDTO {
    private Long id;
    private String entertainmentPlaceName;
    private String entertainmentActivityName;
    private Date reservationDate;
    private Integer reservationHour;
}
