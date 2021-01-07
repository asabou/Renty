package com.mydegree.renty.service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDetailsDTO {
    private Long id;

    private UserDTO user;

    private String firstName;

    private String lastName;

    private String email;

    private String telNumber;
}
