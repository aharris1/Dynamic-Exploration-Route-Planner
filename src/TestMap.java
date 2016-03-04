/**
 * Last Modified: 24 February 2016
 */
public class TestMap {
    static int[] solarSystemIDs = {1, 2, 3, 4, 5, 6};
    static double[] inherentDesirabilities = {.5, .1, 1.5, .7, .1, .6};
    static int[][] links = {new int[]{1,2}, new int[]{2,4}, new int[]{2,5}, new int[]{4,5}, new int[]{5,6}, new int[]{5,3}};

    public static Universe getUniverse(){
        Universe universe = new Universe();
        SolarSystem[] solarSystems = new SolarSystem[solarSystemIDs.length];
        //Iterates to the end of the first list
        for(int i = 0; i < solarSystemIDs.length && i < inherentDesirabilities.length; i++){
            solarSystems[i] = new SolarSystem(solarSystemIDs[i],inherentDesirabilities[i]);
        }
        universe.addSolarSystems(solarSystems, links);
        return universe;
    }
}
