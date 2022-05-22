package com.quasaroperation.service;

import com.quasaroperation.service.impl.LocationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

    @InjectMocks
    LocationServiceImpl locationService;

    @Mock
    PropertiesService propertiesService;

    @Mock
    private Environment environment;

    private double[][] positions;

    @Before
    public void setup(){
        positions = new double[][]{{-500, -200}, {100, -100}, {500, 100}};
    }

    @Test
    public void getLocationSuccessTest() {
        List<Double> distances = Arrays.asList(110.0, 115.5, 142.7);
        when(propertiesService.getDefaultSatellitePositions()).thenReturn(positions);
        Assert.assertEquals(-42.95642775321497, locationService.getLocation(distances).getX(), 0.1);
        Assert.assertEquals(-64.39708805779009, locationService.getLocation(distances).getY(), 0.1);
    }

    @Test(expected = ResponseStatusException.class)
    public void getLocationErrorTest() {
        List<Double> distances = Arrays.asList(110.0, 115.5);
        when(propertiesService.getDefaultSatellitePositions()).thenReturn(positions);
        locationService.getLocation(distances);
    }

}
