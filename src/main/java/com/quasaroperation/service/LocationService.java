package com.quasaroperation.service;

import com.quasaroperation.model.Position;

import java.util.List;

public interface LocationService {

    /**
     * Method to obtain the location according to the distances and the position of the reference satellites
     *
     * @param distances
     * @return The position or coordinate of the spaceship obtained through Trilateration
     */
    Position getLocation(List<Double> distances);

}
