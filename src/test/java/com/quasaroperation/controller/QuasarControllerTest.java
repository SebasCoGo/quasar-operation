package com.quasaroperation.controller;


import com.quasaroperation.model.Position;
import com.quasaroperation.model.Satellite;
import com.quasaroperation.model.SatelliteListRequest;
import com.quasaroperation.model.SpaceshipResponse;
import com.quasaroperation.service.LocationService;
import com.quasaroperation.service.MessageService;
import com.quasaroperation.service.PropertiesService;
import com.quasaroperation.service.SatelliteService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuasarControllerTest {

    @InjectMocks
    QuasarController quasarController;

    @Mock
    LocationService locationService;

    @Mock
    MessageService messageService;

    @Mock
    SatelliteService satelliteService;

    @Mock
    PropertiesService propertiesService;

    @Mock
    private Environment environment;

    private Satellite kenobi;
    private Satellite skywalker;
    private Satellite sato;
    private String secretMessage;
    private Position position;
    private List<String> defaultSatelliteNames;

    @Before
    public void setup() {
        kenobi = new Satellite();
        kenobi.setName("kenobi");
        skywalker = new Satellite();
        skywalker.setName("skywalker");
        sato = new Satellite();
        sato.setName("sato");
        secretMessage = "este es un mensaje secreto";
        position = new Position(-42.95642775321497, -64.39708805779009);
        defaultSatelliteNames = Arrays.asList(kenobi.getName(), skywalker.getName(), sato.getName());
        when(propertiesService.getDefaultSatelliteNames()).thenReturn(defaultSatelliteNames);
        when(locationService.getLocation(Mockito.any())).thenReturn(position);
        when(messageService.getMessage(Mockito.any())).thenReturn(secretMessage);
    }

    @Test
    public void topSecretSuccessTest() {
        SatelliteListRequest satelliteListRequest = new SatelliteListRequest();
        satelliteListRequest.setSatellites(Arrays.asList(kenobi, skywalker, sato));
        ResponseEntity<?> responseEntity = quasarController.topSecret(satelliteListRequest);
        Assert.assertEquals(secretMessage, ((SpaceshipResponse) responseEntity.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(position.getX(), ((SpaceshipResponse) responseEntity.getBody()).getPosition().getX(), 0.1);
        Assert.assertEquals(position.getY(), ((SpaceshipResponse) responseEntity.getBody()).getPosition().getY(), 0.1);
    }

    @Test
    public void topSecreteDuplicatedErrorTest() {
        SatelliteListRequest satelliteListRequest = new SatelliteListRequest();
        satelliteListRequest.setSatellites(Arrays.asList(kenobi, skywalker, sato, kenobi));
        ResponseEntity<?> responseEntity = quasarController.topSecret(satelliteListRequest);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void topSecreteNotFoundErrorTest() {
        SatelliteListRequest satelliteListRequest = new SatelliteListRequest();
        Satellite otherSatellite = new Satellite();
        otherSatellite.setName("otherSatellite");
        satelliteListRequest.setSatellites(Arrays.asList(otherSatellite, skywalker, sato));
        ResponseEntity<?> responseEntity = quasarController.topSecret(satelliteListRequest);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void topSecretSplitSuccessTest() {
        when(satelliteService.findAllByName(defaultSatelliteNames)).thenReturn(Arrays.asList(kenobi, skywalker, sato));
        ResponseEntity<?> responseEntity = quasarController.topSecretSplitGet();
        Assert.assertEquals(secretMessage, ((SpaceshipResponse) responseEntity.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(position.getX(), ((SpaceshipResponse) responseEntity.getBody()).getPosition().getX(), 0.1);
        Assert.assertEquals(position.getY(), ((SpaceshipResponse) responseEntity.getBody()).getPosition().getY(), 0.1);
    }


    @Test
    public void topSecretSplitInsufficientErrorTest() {
        when(satelliteService.findAllByName(defaultSatelliteNames)).thenReturn(Arrays.asList(kenobi, skywalker));
        ResponseEntity<?> responseEntity = quasarController.topSecretSplitGet();
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
