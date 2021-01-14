package com.mydegree.renty.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @ManyToOne
    private EntertainmentActivityPlaceEntity entertainmentActivityPlace;

    @Column(name = "reservation_date")
    private Timestamp reservationDate;

    @Column(name = "reservation_hour")
    private Integer reservationHour;

    @OneToOne(targetEntity = UserDetailsEntity.class, fetch = FetchType.LAZY)
    private UserDetailsEntity userDetails;
}
