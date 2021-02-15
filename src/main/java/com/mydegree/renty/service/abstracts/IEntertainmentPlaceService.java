package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;

import java.util.List;

public interface IEntertainmentPlaceService {
    List<EntertainmentPlaceDTO> findAllEntertainmentPlaces();
    List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(final String token);
    void saveEntertainmentPlace(final EntertainmentPlaceInputDTO entertainmentPlace);
    void deleteEntertainmentPlaceById(final Long id);
    EntertainmentPlaceDTO findById(final Long id);
    void updateEntertainmentPlace(final EntertainmentPlaceDTO entertainmentPlaceDTO);
    List<EntertainmentPlaceDTO> searchEntertainmentPlacesByName(final String name);
    List<EntertainmentPlaceDTO> searchEntertainmentPlacesByActivity(final String activity);
    List<EntertainmentPlaceDTO> searchEntertainmentPlacesByNameAndActivity(final String name, final String activity);
    EntertainmentPlaceDTO findEntertainmentPlaceByName(final String name);
}
