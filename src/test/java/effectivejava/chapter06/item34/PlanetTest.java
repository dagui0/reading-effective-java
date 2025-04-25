package effectivejava.chapter06.item34;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanetTest {

    @Test
    public void testPlanetGravity() {

        double weight = 185.0; // weight in kg
        double mass = weight / Planet.EARTH.surfaceGravity();

        assertEquals(18.8747, mass, 0.0001);

        @SuppressWarnings("deprecation")
        Map<Planet, Double> planetWeights = Map.of(
                Planet.MERCURY,      69.9127
                ,Planet.VENUS,      167.4344
                ,Planet.EARTH,      185.0000
                ,Planet.MARS,        70.2267
                ,Planet.JUPITER,    467.9906
                ,Planet.SATURN,     197.1201
                ,Planet.URANUS,     167.3982
                ,Planet.NEPTUNE,    210.2087
                ,Planet.PLUTO,       11.6675
        );

        for (Planet planet : Planet.values()) {
            assertEquals(planetWeights.get(planet),
                    planet.surfaceWeight(mass), 0.0001);
        }
    }
}
