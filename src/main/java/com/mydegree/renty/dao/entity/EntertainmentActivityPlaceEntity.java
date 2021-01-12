package com.mydegree.renty.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "entertainment_activities_places")
@IdClass(EntertainmentActivityPlaceId.class)
public class EntertainmentActivityPlaceEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "entertainment_activity_id", referencedColumnName = "id")
    private EntertainmentActivityEntity entertainmentActivity;

    @Id
    @ManyToOne
    @JoinColumn(name = "entertainment_place_id", referencedColumnName = "id")
    private EntertainmentPlaceEntity entertainmentPlace;

    @Column(name = "price")
    private Double pricePerHour;

    @Column(name = "max_people_allowed")
    private Integer maxPeopleAllowed;
}
