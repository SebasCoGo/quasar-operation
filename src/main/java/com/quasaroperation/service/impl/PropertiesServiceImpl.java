package com.quasaroperation.service.impl;

import com.quasaroperation.model.SatelliteDTO;
import com.quasaroperation.service.PropertiesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertiesServiceImpl implements PropertiesService {

    @Value("${kenobi.position}")
    private double[] kenobiPosition;

    @Value("${skywalker.position}")
    private double[] skywalkerPosition;

    @Value("${sato.position}")
    private double[] satoPosition;

    @Value("${kenobi.name}")
    private String kenobiName;

    @Value("${skywalker.name}")
    private String skywalkerName;

    @Value("${sato.name}")
    private String satoName;

    @Value("${exception.location.error}")
    private String locationErrorMsg;

    @Value("${exception.message.error}")
    private String messageErrorInfo;

    @Value("${satellite.not-found.error}")
    private String satelliteNotFoundMsg;

    @Value("${satellite.insufficient.error}")
    private String satelliteInsufficientMsg;

    @Value("${satellite.saved-updated.message}")
    private String satelliteSavedUpdatesMsg;

    @Override
    public List<String> getDefaultSatelliteNames() {
        return getDefaultSatellitesInfo().stream().map(SatelliteDTO::getName).collect(Collectors.toList());
    }

    @Override
    public double[][] getDefaultSatellitePositions() {
        List<SatelliteDTO> defaultSatellitesInfo = getDefaultSatellitesInfo();
        double[][] positions = {defaultSatellitesInfo.get(0).getPosition(), defaultSatellitesInfo.get(1).getPosition(), defaultSatellitesInfo.get(2).getPosition()};
        return positions;
    }

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

    public String getLocationErrorMsg() {
        return locationErrorMsg;
    }

    public void setLocationErrorMsg(String locationErrorMsg) {
        this.locationErrorMsg = locationErrorMsg;
    }

    public String getMessageErrorInfo() {
        return messageErrorInfo;
    }

    public void setMessageErrorInfo(String messageErrorInfo) {
        this.messageErrorInfo = messageErrorInfo;
    }

    public String getSatelliteNotFoundMsg() {
        return satelliteNotFoundMsg;
    }

    public void setSatelliteNotFoundMsg(String satelliteNotFoundMsg) {
        this.satelliteNotFoundMsg = satelliteNotFoundMsg;
    }

    public String getSatelliteInsufficientMsg() {
        return satelliteInsufficientMsg;
    }

    public void setSatelliteInsufficientMsg(String satelliteInsufficientMsg) {
        this.satelliteInsufficientMsg = satelliteInsufficientMsg;
    }

    public String getSatelliteSavedUpdatesMsg() {
        return satelliteSavedUpdatesMsg;
    }

    public void setSatelliteSavedUpdatesMsg(String satelliteSavedUpdatesMsg) {
        this.satelliteSavedUpdatesMsg = satelliteSavedUpdatesMsg;
    }
}
