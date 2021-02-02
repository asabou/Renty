package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;

import java.util.List;

public interface IEntertainmentPlaceService {
    List<EntertainmentPlaceDTO> findAllEntertainmentPlaces();
    List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(final String token);
    List<EntertainmentPlaceDTO> findAllEntertainmentPlacesByAddressOrNameOrDescriptionOrUserDetailsFirstNameOrUserDetailsLastName(final String string);
    void saveEntertainmentPlace(final EntertainmentPlaceInputDTO entertainmentPlace);
    void deleteEntertainmentPlaceByName(final String name);
    void deleteEntertainmentPlaceById(final Long id);
    EntertainmentPlaceDTO findById(final Long id);
}
