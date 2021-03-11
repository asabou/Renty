package com.mydegree.renty.service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomEntertainmentActivityDTO {
    private String entertainmentActivity;
    private String entertainmentActivityDescription;
    private Long nrReservations;
}
