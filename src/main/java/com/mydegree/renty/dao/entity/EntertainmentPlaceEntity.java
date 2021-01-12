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
@Table(name = "entertainment_places")
public class EntertainmentPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @OneToOne(targetEntity = AddressEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AddressEntity address;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetailsEntity userDetails;

    @OneToMany(mappedBy = "entertainmentPlace", cascade =  { CascadeType.MERGE, CascadeType.REMOVE })
    private Set<EntertainmentActivityPlaceEntity> entertainmentActivityPlaceEntities = new HashSet<>();
}
