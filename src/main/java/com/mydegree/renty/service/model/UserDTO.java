package com.mydegree.renty.service.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Set<RoleDTO> roles;
}
