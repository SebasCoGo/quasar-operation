package com.quasaroperation.service.impl;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.quasaroperation.model.Position;
import com.quasaroperation.service.PropertiesService;
import com.quasaroperation.service.LocationService;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private Environment environment;

    /**
     * @see LocationService#getLocation(List)
     */
    @Override
    public Position getLocation(List<Double> distances) {
        try {
            double[][] positions = propertiesService.getDefaultSatellitePositions();
            double[] distancesArray = distances.stream().mapToDouble(d -> d).toArray();
            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distancesArray), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();
            double[] position = optimum.getPoint().toArray();
            return new Position(position[0], position[1]);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty("exception.location.error"));
        }
    }
}
