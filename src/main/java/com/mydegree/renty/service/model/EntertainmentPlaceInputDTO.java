package com.mydegree.renty.service.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntertainmentPlaceInputDTO {
    private Long userDetailsId;
    private String name;
    private String description;
    private byte[] profileImage;
    private AddressDTO address;
    private String entertainmentActivity;
    private Double pricePerHour;
    private Integer maxPeopleAllowed;
}
