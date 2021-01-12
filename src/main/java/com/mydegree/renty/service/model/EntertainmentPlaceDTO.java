package com.mydegree.renty.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EntertainmentPlaceDTO {
    private Long id;

    private String name;

    private String description;

    private byte[] profileImage;

    private AddressDTO address;

    private UserDetailsDTO userDetails;
}
