package com.mydegree.renty.service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntertainmentActivityInputDTO {
    private Long entertainmentActivityId;
    private Long entertainmentPlaceId;
    private Double price;
    private Integer maxPeopleAllowed;
}
