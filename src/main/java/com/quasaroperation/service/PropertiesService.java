package com.quasaroperation.service;

import java.util.List;

public interface PropertiesService {

    /**
     * Method to get the names of the default satellites
     *
     * @return List of default satellite names
     */
    List<String> getDefaultSatelliteNames();

    /**
     * Method to get the positions of the default satellites
     *
     * @return Two-dimensional array with default satellite positions
     */
    double[][] getDefaultSatellitePositions();

}
