package com.quasaroperation.service;

import com.quasaroperation.model.Satellite;

import java.util.List;
import java.util.Optional;

public interface SatelliteService {

    List<Satellite> findAll();

    List<Satellite> findAllByName(List<String> names);
    Optional<Satellite> findByName(String name);
    Satellite save(Satellite satellite);

}
