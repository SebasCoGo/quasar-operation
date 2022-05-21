package com.quasaroperation.service.impl;

import com.quasaroperation.model.Satellite;
import com.quasaroperation.repository.SatelliteRepository;
import com.quasaroperation.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SatelliteServiceImpl implements SatelliteService {

    @Autowired
    private SatelliteRepository satelliteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Satellite> findAll() {
        return satelliteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Satellite> findAllByName(List<String> names) {
        return satelliteRepository.findAllById(names);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Satellite> findByName(String name) {
       return satelliteRepository.findById(name);
    }

    @Override
    @Transactional
    public Satellite save(Satellite satellite) {
        return satelliteRepository.save(satellite);
    }
}
