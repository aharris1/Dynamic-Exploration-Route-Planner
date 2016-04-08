/**
 * Created by Andrew Harris 2/24/2016
 * Last Modified: 2/27/2016
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        workFlow();
    }

    public static void workFlow(){
        /*
         * Generate universe
         * Value systems
         * Get current system
         * Generate route starting from that system
         */
        Universe universe = CSVtoUniverse.generateUniverse();
        universe.applyJumps(XMLAPIParser.getJumps());
        universe.applyKills(XMLAPIParser.getKills());
        SolarSystem testSystem = universe.getSolarSystem(235234);
    }


}
