package com.mydegree.renty.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddressDTO {
    private Long id;

    private String county;

    private String city;

    private String street;

    private String number;

    private EntertainmentPlaceDTO entertainmentPlace;
}
