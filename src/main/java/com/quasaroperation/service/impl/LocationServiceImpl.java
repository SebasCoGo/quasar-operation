package com.quasaroperation.service.impl;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.quasaroperation.model.Position;
import com.quasaroperation.service.LocationService;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Value("${kenobi.position}")
    private double[] KENOBI_POSITION;

    @Value("${skywalker.position}")
    private double[] SKYWALKER_POSITION;

    @Value("${sato.position}")
    private double[] SATO_POSITION;

    @Override
    public Position getLocation(List<Double> distances) {
        double[][] positions = new double[][]{KENOBI_POSITION, SKYWALKER_POSITION, SATO_POSITION};
        double[] distancesArray = distances.stream().mapToDouble(d -> d).toArray();
        try {
            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distancesArray), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            double[] position = optimum.getPoint().toArray();

            return new Position(position[0], position[1]);
        } catch (IllegalArgumentException e) {
            //throw new LocationException("No hay suficiente información para calcular una posición.", e);
        }
        return null;
    }

}
