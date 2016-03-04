import java.util.*;

/**
 * Created by Andrew Harris on 2/24/2016.
 * Last Modified on 2/24/2016
 */
public class Universe {
    final private int MAX_RADIATION_RANGE = 5; //Sets a constant for the maximum radiation range
    private Map<Integer, SolarSystem> IDTable;

    /**
     * Initialises an empty universe
     */
    public Universe(){
        IDTable = new HashMap<Integer, SolarSystem>();
    }

    public Universe(Set<SolarSystem>  solarSystems){
        IDTable = new HashMap<Integer, SolarSystem>();
        for(SolarSystem solarSystem : solarSystems){
            IDTable.put(solarSystem.getID(), solarSystem);
        }
    }
    /**
     * Initialises a universe with initial solar systems added in
     * @param solarSystems the solar systems to add to the universe at creation
     */
    public Universe(SolarSystem[] solarSystems){
        //Initialises the HashMap
        IDTable = new HashMap<Integer, SolarSystem>();
        //Adds the new systems to the map
        addSolarSystems(solarSystems);
    }

    /**
     * Adds an array of solar systems to the universe
     * @param solarSystems the solar systems to add to the universe
     */
    public void addSolarSystems(SolarSystem[] solarSystems){
        for(SolarSystem solarSystem : solarSystems){
            IDTable.put(solarSystem.getID(), solarSystem);
        }
    }

    /**
     * Adds an array of solar systems and an array of ID#-pairs representing the links
     * @param solarSystems the solar systems to add to the universe at the start
     * @param links the links to make between added systems
     */
    public void addSolarSystems(SolarSystem[] solarSystems, int[][] links){
        addSolarSystems(solarSystems);
        addConnection(links);
    }

    /**
     * Returns a solar system based on its ID
     * @param ID the ID of the solar system required
     * @return the solar system with that ID
     */
    public SolarSystem getSolarSystem(int ID){
        return IDTable.get(ID);
    }

    /**
     * Adds a reciprocated connection between two different systems
     * @param fromID the ID# of the originating system
     * @param toID the ID# of the destination system
     */
    public void addConnection(int fromID, int toID){
        //Let A = from, B = to.  Goal: A<-->B
        IDTable.get(fromID).addConnectedSystem(IDTable.get(toID)); //Creates connection A-->B
        IDTable.get(toID).addConnectedSystem(IDTable.get(fromID)); //Creates connection B-->A
    }

    /**
     * Adds multiple reciprocated connections
     * @param links an array of 2-element arrays containing pairs of ID#'s that represent links between two systems
     */
    public void addConnection(int[][] links){
        for(int i = 0; i < links.length; i++){
            if(links[i].length == 2){
                addConnection(links[i][0], links[i][1]);
            }
            else{
                //TODO throw an error that describes the invalid link
            }
        }
    }
//
//    /**
//     * Has each system spread radiation to every system within a range set by a constant above
//     */
//    public void applyEffectRadiation(){
//        Iterator<SolarSystem> iterator = IDTable.values().iterator(); //Gets an iterator for
//        while(iterator.hasNext()){
//            iterator.next().applyRadiatedEffects(MAX_RADIATION_RANGE);
//        }
//    }

    public void applyJumps(int[][] jumps){
        for(int i = 0; i < jumps.length; i++){
            getSolarSystem(jumps[i][0]).setJumps(jumps[i][1]);
        }
    }

    public void applyKills(int[][] kills){
        for(int i = 0; i < kills.length; i++){
            SolarSystem solarSystem = getSolarSystem(kills[i][0]);
            solarSystem.setKills(kills[i][1]);
            solarSystem.setPods(kills[i][2]);
        }
    }

    public SolarSystem[][] getSecurityCategories(){
        Iterator<SolarSystem> systemsIterator = IDTable.values().iterator(); //Gets an iterator for the systems
        ArrayList<SolarSystem> highSec = new ArrayList<SolarSystem>();
        ArrayList<SolarSystem> lowSec = new ArrayList<SolarSystem>();
        ArrayList<SolarSystem> nullSec = new ArrayList<SolarSystem>();
        while(systemsIterator.hasNext()){
            SolarSystem solarSystem = systemsIterator.next();
            double security = solarSystem.getSecurity();
            if(security <= 0.01){
                nullSec.add(solarSystem);
            }
            else if(security <= .5){
                lowSec.add(solarSystem);
            }
            else{
                highSec.add(solarSystem);
            }
        }
        SolarSystem[][] returnArray = new SolarSystem[3][];
        returnArray[0] = new SolarSystem[highSec.size()];
        returnArray[1] = new SolarSystem[lowSec.size()];
        returnArray[2] = new SolarSystem[nullSec.size()];

        highSec.toArray(returnArray[0]);
        lowSec.toArray(returnArray[1]);
        nullSec.toArray(returnArray[2]);
        return returnArray;
    }

    //TODO Implement std.dev and averaging functions using Apache Commons Math Library
    public double[] calculateKillStats(SolarSystem[] securityCategorySystems){
        int killAccumulator = 0;
        int[] killsArray = new int[securityCategorySystems.length];
        for(int i = 0; i < securityCategorySystems.length; i++){
            killAccumulator += securityCategorySystems[i].getKills();
            killsArray[i] = securityCategorySystems[i].getKills();
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer("ID, inherentDesirability, relationalDesirability, totalDesirability\n");
        Iterator<SolarSystem> iterator = IDTable.values().iterator();
        while(iterator.hasNext()){
            buffer.append(iterator.next().toString() + "\n");
        }
        return buffer.toString();
    }

    public Map<Integer, SolarSystem> getIDTable(){
        return IDTable;
    }
}
