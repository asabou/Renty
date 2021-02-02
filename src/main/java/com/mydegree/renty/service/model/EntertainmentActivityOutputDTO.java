package com.mydegree.renty.service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntertainmentActivityOutputDTO {
    private Long entertainmentActivityId;
    private String entertainmentActivityName;
    private String entertainmentActivityDescription;
    private Double entertainmentActivityPrice;
    private Integer maxPeopleAllowed;
}
