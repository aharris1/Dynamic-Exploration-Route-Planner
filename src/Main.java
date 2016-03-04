/**
 * Created by Andrew Harris 2/24/2016
 * Last Modified: 2/27/2016
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    /*
    Current state of progress:
    CAN:
      Use Universe class to store and interact with the map as a whole.
      Generate all routes from a particular solar system
      Score each route
      Determine which route is optimal
    TODO: Import New Eden from SDE and convert to Universe class data structure
    TODO: Import current position from CREST
    TODO: Set waypoint using CREST
    TODO: Gather system data using CREST
     */


    public static void main(String[] args) {
//        File csvData = new File("C:\\Users\\Class2018\\Downloads\\mapSolarSystemJumps\\mapSolarSystemJumps.csv");
//
//        try {
//            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
//            List<CSVRecord> list = parser.getRecords();
//            for(int i = 0; i < list.size(); i++){
//                System.out.println(list.get(i).get(1).toString());
//            }
//
//        }
//        catch(Exception e) {
//            System.out.println(e);
//        }
//        Universe universe = TestMap.getUniverse();
//        System.out.print(universe.toString());
//        System.out.print(universe.toString());
//        LinkedList<SolarSystem>[] routes = RouteGenerator.generateRoutes(universe.getSolarSystem(2), 3);
//        System.out.println("Score, routeA, routeB, ... , routeN");
//        System.out.println(RouteGenerator.printRoute(RouteGenerator.selectBestRoute(routes), true));
////        CSVtoUniverse.generateUniverse();
//        CRESTAuthenticator authenticator = new CRESTAuthenticator("29823546");
////        System.out.println(authenticator.start());
////        System.out.println(Arrays.toString(authenticator.authorize("_alJXeb0LUnCRR6RNVKUny3E0CPb_vSruNz5914xUMD6dtEl30PMBlshJCwHIaI10")));
////        System.out.println(authenticator.refresh("oUAYWSelFM2yTxjY8MWMrzWnFu0TYAj11h4Ce3khNbWEUmTbvrA0g81oGqOi5xaU0"));
//        System.out.println(CRESTInterface.currentLocation("kMAnkvfCg0MvLTuPFW-pAOkXrA7kJ5ak35DwvOaPQkzqV6nZMe6AF7Q3mYl-lde6BcQgJGnTQR6tNSHzTASCAw2",93362631));

//        CRESTInterface.addWaypoint("u3bxEwjJtJent8IjnqmsZRwpMrzBboTHY6sFwfXVIISpsG23bGJGEmxfLYRU7M-EEeNQSgsWAPpmCP6NYoRRpg2", 94052458, 30004273);
//        int[][] xmlKills = XMLAPIParser.getKills();
//        int[][] xmlJumps = XMLAPIParser.getJumps();
//        System.out.println(Arrays.deepToString(xmlKills));
//        System.out.println(Arrays.deepToString(xmlJumps));
//        System.out.println(CRESTAuthenticator.getEncodedID());
//        Universe universe = CSVtoUniverse.generateUniverse();
//        universe.applyJumps(XMLAPIParser.getJumps());
//        universe.applyKills(XMLAPIParser.getKills());
//        System.out.println(Arrays.toString(universe.getSecurityCategories()));
        workFlow();
    }

    public static void workFlow(){
        /*
         * Generate universe
         * Value systems
         * Get current system
         * Generate route starting from that system
         */
//        final int CHARACTER_ID = 93362631; //For proof-of-concept, have it found by the user and plugged into the interface
//        CRESTAuthenticator authenticator = new CRESTAuthenticator("29823546");
//        System.out.println(authenticator.start());
//        String accessToken = authenticator.refresh("06LdGUecWJdrmjY62LLDr8Wiyye3F4OGhpyMU2BxVKsN3Futh0hUg0zMTJ6cOH9F0");
//        System.out.println(accessToken);
//        String authCode = Arrays.deepToString((authenticator.authorize("_d0Zu6yeBbmfddJoZXL7gU_H5V1_kvIvnsO3dzbDky8KfC2cY8-v2U03pcazTK6-klYMo1ZaGDkgQdTc3bbhfg2")));
//        System.out.print(authCode);
        Universe universe = CSVtoUniverse.generateUniverse();
        universe.applyJumps(XMLAPIParser.getJumps());
        universe.applyKills(XMLAPIParser.getKills());
        SolarSystem testSystem = universe.getSolarSystem(235234);
//        SolarSystem currentSystem = universe.getSolarSystem(CRESTInterface.currentLocation(accessToken, CHARACTER_ID));
    }


}
