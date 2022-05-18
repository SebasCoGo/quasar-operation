package com.quasaroperation.service;

import com.quasaroperation.model.Position;

import java.util.List;

public interface LocationService {

    Position getLocation(List<Double> distances);

}
