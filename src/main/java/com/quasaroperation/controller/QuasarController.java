package com.quasaroperation.controller;

import com.quasaroperation.model.Satellite;
import com.quasaroperation.model.SatelliteListRequest;
import com.quasaroperation.model.SpaceshipResponse;
import com.quasaroperation.service.LocationService;
import com.quasaroperation.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class QuasarController {

    @Autowired
    LocationService locationService;

    @Autowired
    MessageService messageService;

    @PostMapping("/topsecret")
    public ResponseEntity<SpaceshipResponse> topSecret(@RequestBody SatelliteListRequest satelliteListRequest) {
        SpaceshipResponse spaceshipResponse = new SpaceshipResponse();
        spaceshipResponse.setPosition(locationService.getLocation(satelliteListRequest.getSatellites().stream().map(Satellite::getDistance).collect(Collectors.toList())));
        spaceshipResponse.setMessage(messageService.getMessage(satelliteListRequest.getSatellites().stream().map(Satellite::getMessage).collect(Collectors.toList())));
        return ResponseEntity.ok().body(spaceshipResponse);
    }

}
