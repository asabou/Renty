package com.mydegree.renty.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "entertainment_activities")
public class EntertainmentActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "entertainmentActivity", cascade = CascadeType.MERGE)
    private Set<EntertainmentActivityPlaceEntity> entertainmentActivityPlaceEntities = new HashSet<>();
}
