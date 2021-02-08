package com.mydegree.renty.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String role;
    private String description;
    public RoleDTO(String role) {
        this.role = role;
    }
}
