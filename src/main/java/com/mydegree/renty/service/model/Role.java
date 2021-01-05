package com.mydegree.renty.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role {
    private Long id;
    private String role;
    public Role(String role) {
        this.role = role;
    }
}
