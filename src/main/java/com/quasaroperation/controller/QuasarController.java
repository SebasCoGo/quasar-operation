package com.quasaroperation.controller;

import com.quasaroperation.model.*;
import com.quasaroperation.service.PropertiesService;
import com.quasaroperation.service.LocationService;
import com.quasaroperation.service.MessageService;
import com.quasaroperation.service.SatelliteService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "${api.topsecret.description}", response = SpaceshipResponse.class)
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
            return getResponseEntitySpaceship(spaceshipResponse, satellitesRequestFiltered);
        } else {
            return getResponseError(environment.getProperty("satellite.not-match.error"));
        }
    }

    @PostMapping("/topsecret_split/{satelliteName}")
    @ApiOperation("${api.topsecret-split-post.description}")
    public ResponseEntity<?> topSecretSplitPost(@PathVariable("satelliteName") String satelliteName,
                                                @RequestBody SatelliteRequest satelliteRequest) {
        Satellite satelliteModel = new Satellite();
        satelliteModel.setName(satelliteName);
        satelliteModel.setMessage(satelliteRequest.getMessage());
        satelliteModel.setDistance(satelliteRequest.getDistance());
        satelliteService.save(satelliteModel);
        return getResponseOk(environment.getProperty("satellite.saved-updated.message"));
    }

    @GetMapping("/topsecret_split")
    @ApiOperation(value = "${api.topsecret-split-get.description}", response = SpaceshipResponse.class)
    public ResponseEntity<?> topSecretSplitGet() {
        List<String> defaultSatelliteNames = propertiesService.getDefaultSatelliteNames();
        List<Satellite> satelliteModels = satelliteService.findAllByName(defaultSatelliteNames);
        SpaceshipResponse spaceshipResponse = new SpaceshipResponse();
        if (satelliteModels.size() == defaultSatelliteNames.size()) {
            return getResponseEntitySpaceship(spaceshipResponse, satelliteModels);
        } else {
            return getResponseError(environment.getProperty("satellite.insufficient.error"));
        }
    }

    @GetMapping("/topsecret_split_find_all")
    @ApiOperation(value = "${api.topsecret-split-find-all-description}", response = SatelliteListResponse.class)
    public ResponseEntity<?> topSecretSplitFindAll() {
        SatelliteListResponse satellitesListResponse = new SatelliteListResponse();
        List<Satellite> satellitesSaved = satelliteService.findAll();
        satellitesListResponse.setSatellites(satellitesSaved);
        return ResponseEntity.ok().body(satellitesListResponse);
    }

    @DeleteMapping("/topsecret_split_delete/{satelliteName}")
    @ApiOperation(value = "${api.topsecret-split-delete.description}")
    public ResponseEntity<?> topSecretSplitDelete(@PathVariable("satelliteName") String satelliteName) {
        if (satelliteService.findByName(satelliteName).isPresent()) {
            satelliteService.deleteByName(satelliteName);
            return getResponseOk(environment.getProperty("satellite.deleted.message"));
        } else {
            return getResponseError(environment.getProperty("satellite.not-found.error"));
        }
    }

    private ResponseEntity<?> getResponseEntitySpaceship(SpaceshipResponse spaceshipResponse, List<Satellite> satellitesInfo) {
        satellitesInfo.sort(Comparator.comparing(Satellite::getName));
        spaceshipResponse.setPosition(locationService.getLocation(satellitesInfo.stream().
                map(Satellite::getDistance).collect(Collectors.toList())));
        spaceshipResponse.setMessage(messageService.getMessage(satellitesInfo.stream().
                map(Satellite::getMessage).collect(Collectors.toList())));
        return ResponseEntity.ok().body(spaceshipResponse);
    }

    private ResponseEntity<?> getResponseOk(String message) {
        return ResponseEntity.ok().body(message);
    }

    private ResponseEntity<?> getResponseError(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}
