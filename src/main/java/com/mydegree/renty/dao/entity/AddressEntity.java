package com.mydegree.renty.dao.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    private String county;

    private String city;

    private String street;

    private String number;

    @OneToOne(targetEntity = EntertainmentPlaceEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EntertainmentPlaceEntity entertainmentPlace;
}
