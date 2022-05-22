package com.quasaroperation.service.impl;

import com.quasaroperation.model.SatelliteDTO;
import com.quasaroperation.service.PropertiesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertiesServiceImpl implements PropertiesService {

    @Value("${kenobi.name}")
    private String kenobiName;

    @Value("${kenobi.position}")
    private double[] kenobiPosition;

    @Value("${skywalker.name}")
    private String skywalkerName;

    @Value("${skywalker.position}")
    private double[] skywalkerPosition;

    @Value("${sato.name}")
    private String satoName;

    @Value("${sato.position}")
    private double[] satoPosition;

    /**
     * @see PropertiesService#getDefaultSatelliteNames()
     */
    @Override
    public List<String> getDefaultSatelliteNames() {
        return getDefaultSatellitesInfo().stream().map(SatelliteDTO::getName).collect(Collectors.toList());
    }


    /**
     * @see PropertiesService#getDefaultSatellitePositions()
     */
    @Override
    public double[][] getDefaultSatellitePositions() {
        List<SatelliteDTO> defaultSatellitesInfo = getDefaultSatellitesInfo();
        int columSize = 2;
        double[][] defaultPositions = new double[defaultSatellitesInfo.size()][columSize];
        for (int i = 0; i < columSize; i++) {
            defaultSatellitesInfo.forEach(s -> defaultPositions[defaultSatellitesInfo.indexOf(s)] = s.getPosition());
        }
        return defaultPositions;
    }

    /**
     * Method to get the information of the default satellites
     *
     * @return List of DOTs with the information of the default satellites
     */
    protected List<SatelliteDTO> getDefaultSatellitesInfo() {
        List<SatelliteDTO> defaultSatellites = Arrays.asList(new SatelliteDTO(getKenobiName(), getKenobiPosition()), new SatelliteDTO(getSkywalkerName(), getSkywalkerPosition()), new SatelliteDTO(getSatoName(), getSatoPosition()));
        defaultSatellites.sort(Comparator.comparing(SatelliteDTO::getName));
        return defaultSatellites;
    }

    public double[] getKenobiPosition() {
        return kenobiPosition;
    }

    public void setKenobiPosition(double[] kenobiPosition) {
        this.kenobiPosition = kenobiPosition;
    }

    public double[] getSkywalkerPosition() {
        return skywalkerPosition;
    }

    public void setSkywalkerPosition(double[] skywalkerPosition) {
        this.skywalkerPosition = skywalkerPosition;
    }

    public double[] getSatoPosition() {
        return satoPosition;
    }

    public void setSatoPosition(double[] satoPosition) {
        this.satoPosition = satoPosition;
    }

    public String getKenobiName() {
        return kenobiName;
    }

    public void setKenobiName(String kenobiName) {
        this.kenobiName = kenobiName;
    }

    public String getSkywalkerName() {
        return skywalkerName;
    }

    public void setSkywalkerName(String skywalkerName) {
        this.skywalkerName = skywalkerName;
    }

    public String getSatoName() {
        return satoName;
    }

    public void setSatoName(String satoName) {
        this.satoName = satoName;
    }

}
