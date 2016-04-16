import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;

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
    public static Vector<LinkedList<SolarSystem>> generateRoutes(Vector<LinkedList<SolarSystem>> returnSet, LinkedList<SolarSystem> currentPath, int desiredLength, boolean avoidLow) {
        //if the path is long enough, or if we hit the sanity limit of 20, it's time to return
        //its greater than desired length because we start with our current system
        if(getUniqueCount(currentPath) > desiredLength || currentPath.size() > 20){
            returnSet.add(currentPath);
        }
        else {
            SolarSystem currentSystem = currentPath.getLast();
            for (Iterator<SolarSystem> i = currentSystem.getConnectedSolarSystems().iterator(); i.hasNext();){
                LinkedList<SolarSystem> newPath = new LinkedList<SolarSystem>();
                newPath = (LinkedList<SolarSystem>) currentPath.clone();
                SolarSystem workingSystem = i.next();
                if (avoidLow == true){
                    if(workingSystem.getSecurity() < 0.5){
                        continue;
                    }
                }
                if (currentPath.size() > 1) {
                    SolarSystem parent = currentPath.get(currentPath.size() - 2);
                    if (parent.getID() == workingSystem.getID() && currentSystem.getConnectedSolarSystems().size() > 1){
                        continue;
                    }
                }
                newPath.add(workingSystem);
                generateRoutes(returnSet, newPath, desiredLength, avoidLow);
            }
        }
        return returnSet;
    }

    public static int getUniqueCount(LinkedList<SolarSystem> route){
        HashSet<Integer> workingSet = new HashSet<Integer>();
        Iterator<SolarSystem> routeIterator = route.iterator();
        while(routeIterator.hasNext()){
            workingSet.add(routeIterator.next().getID());
        }
        return workingSet.size();
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
    public static LinkedList<SolarSystem> selectBestRoute(Vector<LinkedList<SolarSystem>> routes){
        int bestIndex = 0;
        double bestScore = -1000000000;
        double routeScore;
        for(int i = 0; i < routes.size(); i++){
            routeScore = evaluateRoutes(routes.elementAt(i));
            if(routeScore > bestScore){
                bestIndex = i;
                bestScore = routeScore;
            }
        }
        return routes.elementAt(bestIndex);
    }
}
