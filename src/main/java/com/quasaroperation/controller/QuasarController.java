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
    private LocationService locationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SatelliteService satelliteService;

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private Environment environment;

    @PostMapping("/topsecret")
    public ResponseEntity<?> topSecret(@RequestBody SatelliteListRequest satelliteListRequest) {
        SpaceshipResponse spaceshipResponse = new SpaceshipResponse();
        List<String> defaultSatelliteNames = propertiesService.getDefaultSatelliteNames();
        List<Satellite> satellitesRequestFiltered = satelliteListRequest.getSatellites().stream()
                .filter(s -> defaultSatelliteNames.contains(s.getName())).collect(Collectors.toList());
        if (!satellitesRequestFiltered.stream().map(Satellite::getName)
                .collect(Collectors.toList()).stream().allMatch(new HashSet<>()::add)) {
            return getResponseError(environment.getProperty("satellite.duplicated.error"));
        }
        if (defaultSatelliteNames.size() == satellitesRequestFiltered.size()) {
            return getResponseEntity(spaceshipResponse, satellitesRequestFiltered);
        } else {
            return getResponseError(environment.getProperty("satellite.not-found.error"));
        }
    }

    @PostMapping("/topsecret_split/{satelliteName}")
    public ResponseEntity<?> topSecretSplitPost(@PathVariable("satelliteName") String satelliteName,
                                                @RequestBody SatelliteRequest satelliteRequest) {
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
            return getResponseEntity(spaceshipResponse, satelliteModels);
        } else {
            return getResponseError(environment.getProperty("satellite.insufficient.error"));
        }
    }

    private ResponseEntity<?> getResponseEntity(SpaceshipResponse spaceshipResponse, List<Satellite> satellitesInfo) {
        satellitesInfo.sort(Comparator.comparing(Satellite::getName));
        spaceshipResponse.setPosition(locationService.getLocation(satellitesInfo.stream().
                map(Satellite::getDistance).collect(Collectors.toList())));
        spaceshipResponse.setMessage(messageService.getMessage(satellitesInfo.stream().
                map(Satellite::getMessage).collect(Collectors.toList())));
        return ResponseEntity.ok().body(spaceshipResponse);
    }

    private ResponseEntity<?> getResponseError(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}
