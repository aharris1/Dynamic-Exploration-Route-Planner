import java.util.*;

/**
 * Created by Andrew Harris on 2/25/2016.
 * Last Modified on 2/25/2016
 */
public class RouteGenerator {

    public static final int RADIATED_EFFECT_RANGE = 5;

    /**
     * Generates an array of routes of a length "length" and starting from Solar System "start"
     * @param start the solar system in which the route begins
     * @param length the length of the route
     * @return array of routes represented by LinkedLists<SolarSystem>
     */
    public static LinkedList<SolarSystem>[] generateRoutes(SolarSystem start, int length) {
        if (length > 0) {
            List<LinkedList<SolarSystem>> routes = new ArrayList<LinkedList<SolarSystem>>();
            //Looks at each
            for (SolarSystem option : start.getConnectedSolarSystems()) {
                //Recursive function: For each option it adds all of the routes that start with that option to the list of routes
                //Decrements the length value to produce end case: length == 0.
                for (LinkedList<SolarSystem> generatedRoute : generateRoutes(option, length - 1)) {
                    LinkedList<SolarSystem> bufferList = new LinkedList<SolarSystem>();
                    bufferList.add(option); //Adds the first node jumped to as the first step to the bufferList
                    bufferList.addAll(generatedRoute); //Adds the routes that the option can produce to the bufferList
                    routes.add(bufferList); //Adds the route stored in the bufferList to the overall list of routes
                }
            }
            LinkedList<SolarSystem>[] returnArray = new LinkedList[routes.size()];
            return routes.toArray(returnArray);
        }
        else {
            LinkedList<SolarSystem>[] returnArray = new LinkedList[1];
            returnArray[0] = new LinkedList<SolarSystem>();
            return returnArray;
        }
    }

    /**
     * Converts an individual route to a string
     * @param route an individual route to convert to CSV form
     * @return CSV-form string containing route information
     */
    public static String printRoute(LinkedList<SolarSystem> route){
        Iterator<SolarSystem> routeIterator = route.iterator();
        StringBuffer stringBuffer = new StringBuffer();
        while(routeIterator.hasNext()){
            stringBuffer.append(routeIterator.next().getID());
            stringBuffer.append(", ");
        }
        return stringBuffer.toString();
    }

    /**
     * Converts an individual route to a string after scoring it
     * @param route an individual route to convert to CSV form
     * @param evaluate whether the route's score should be added to the CSV entry
     * @return CSV-form string containing the requested route information
     */
    public static String printRoute(LinkedList<SolarSystem> route, boolean evaluate){
        StringBuffer returnBuffer = new StringBuffer();
        if(evaluate){
            returnBuffer.append(evaluateRoutes(route));
            returnBuffer.append(", ");
        }
        returnBuffer.append(printRoute(route));
        return returnBuffer.toString();
    }

    /**
     * Produces an array of scores from an array of routes
     * @param routes an array of routes
     * @return a double array containing index-matched scores
     */
    public static double[] evaluateRoutes(LinkedList<SolarSystem>[] routes){
        double[] returnScores = new double[routes.length];
        for(int i = 0; i < routes.length; i++){
            returnScores[i] = evaluateRoutes(routes[i]);
        }
        return returnScores;
    }

    /**
     * Scores an individual route
     * @param route the individual route to score
     * @return the route's score
     */
    public static double evaluateRoutes(LinkedList<SolarSystem> route){
        double routeScore = 0;
        Set<SolarSystem> visitedSystems = new HashSet<SolarSystem>();
        Iterator<SolarSystem> routeIterator = route.iterator();
        while(routeIterator.hasNext()){
            SolarSystem next = routeIterator.next(); //Gets the next system
            visitedSystems.add(next); //Adds it to the list of visited systems
            next.applyRadiatedEffects(RADIATED_EFFECT_RANGE); //Calculates the effects from other nearby systems
            routeScore += next.getTotalDesirability(); //Adds the total desirability of the system to the route score
            next.setVisited(true, true);
        }
        for(SolarSystem solarSystem : visitedSystems){
            solarSystem.reset();
        }
        return routeScore;
    }

    /**
     * Selects and returns the best route of the array of routes provided
     * @param routes an array containing all candidate routes
     * @return the route with the greatest score
     */
    public static LinkedList<SolarSystem> selectBestRoute(LinkedList<SolarSystem>[] routes){
        int bestIndex = 0;
        double bestScore = -1000000000;
        double routeScore;
        for(int i = 0; i < routes.length; i++){
            routeScore = evaluateRoutes(routes[i]);
            if(routeScore > bestScore){
                bestIndex = i;
                bestScore = routeScore;
            }
        }
        return routes[bestIndex];
    }
}
