package com.quasaroperation.controller;

import com.quasaroperation.model.*;
import com.quasaroperation.service.PropertiesService;
import com.quasaroperation.service.LocationService;
import com.quasaroperation.service.MessageService;
import com.quasaroperation.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class QuasarController {

    @Autowired
    LocationService locationService;

    @Autowired
    MessageService messageService;

    @Autowired
    SatelliteService satelliteService;

    @Autowired
    PropertiesService propertiesService;

    @Autowired
    private Environment environment;

    @PostMapping("/topsecret")
    public ResponseEntity<?> topSecret(@RequestBody SatelliteListRequest satelliteListRequest) {
        SpaceshipResponse spaceshipResponse = new SpaceshipResponse();
        List<String> satelliteRequestNames = satelliteListRequest.getSatellites().stream().map(Satellite::getName).collect(Collectors.toList());
        List<String> defaultSatelliteNames = propertiesService.getDefaultSatelliteNames();
        satelliteRequestNames.retainAll(defaultSatelliteNames);
        if (satelliteRequestNames.size() == defaultSatelliteNames.size()) {
            satelliteListRequest.getSatellites().sort(Comparator.comparing(Satellite::getName));
            spaceshipResponse.setPosition(locationService.getLocation(satelliteListRequest.getSatellites().stream().map(Satellite::getDistance).collect(Collectors.toList())));
            spaceshipResponse.setMessage(messageService.getMessage(satelliteListRequest.getSatellites().stream().map(Satellite::getMessage).collect(Collectors.toList())));
            return ResponseEntity.ok().body(spaceshipResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(environment.getProperty("satellite.not-found.error"));
        }

    }

    @PostMapping("/topsecret_split/{satelliteName}")
    public ResponseEntity<?> topSecretSplitPost(@PathVariable("satelliteName") String satelliteName, @RequestBody SatelliteRequest satelliteRequest) {
        Satellite satelliteModel = new Satellite();
        satelliteModel.setName(satelliteName);
        satelliteModel.setMessage(satelliteRequest.getMessage());
        satelliteModel.setDistance(satelliteRequest.getDistance());
        satelliteService.save(satelliteModel);
        return ResponseEntity.ok().body(environment.getProperty("satellite.saved-updated.message"));
    }

    @GetMapping("/topsecret_split")
    public ResponseEntity<?> topSecretSplitGet() {
        List<String> defaultSatelliteNames = propertiesService.getDefaultSatelliteNames();
        List<Satellite> satelliteModels = satelliteService.findAllByName(defaultSatelliteNames);
        SpaceshipResponse spaceshipResponse = new SpaceshipResponse();
        if (satelliteModels.size() == defaultSatelliteNames.size()) {
            satelliteModels.sort(Comparator.comparing(Satellite::getName));
            spaceshipResponse.setPosition(locationService.getLocation(Arrays.asList(satelliteModels.get(0).getDistance(), satelliteModels.get(1).getDistance(), satelliteModels.get(2).getDistance())));
            spaceshipResponse.setMessage(messageService.getMessage(Arrays.asList(satelliteModels.get(0).getMessage(), satelliteModels.get(1).getMessage(), satelliteModels.get(2).getMessage())));
            return ResponseEntity.ok().body(spaceshipResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(environment.getProperty("satellite.insufficient.error"));
        }
    }

}
