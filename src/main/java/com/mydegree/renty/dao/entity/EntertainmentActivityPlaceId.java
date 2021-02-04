package com.mydegree.renty.dao.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
@ToString
public class EntertainmentActivityPlaceId implements Serializable {
    private Long entertainmentActivity;
    private Long entertainmentPlace;
}
