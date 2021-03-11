package com.mydegree.renty.dao.entity.custom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomEntertainmentActivity {
    private String entertainmentActivity;
    private String entertainmentActivityDescription;
    private Long nrReservations;
}
