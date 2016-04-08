import java.util.Set;
import java.util.HashSet;

/**
 * Created by Andrew Harris on 2/23/2016.
 * Last Modified on 2/24/2016
 */
public class SolarSystem {
    private Set<SolarSystem> connectedSolarSystems = new HashSet<SolarSystem>();
    private double inherentDesirability;
    private double inherentDesirabilityRESET;
    private double relationalDesirability = 0;
    private int ID;
    private boolean modified = false;
    private boolean visited = false;
    private int kills;
    private int jumps;
    private int pods;
    private double security;
    private double userThreshold = 5; //default value allows 5 kills in the last hour before giving a hazard rating of 10

    /**
     * Initialises with just the ID
     * @param ID the system ID
     */
    public SolarSystem(int ID){
        this.ID = ID;
    }

    /**
     * Initialises with an inherent desirability
     * @param ID the system ID
     * @param inherentDesirability the desirability of the system as determined by another algorithm
     */
    public SolarSystem(int ID, double inherentDesirability){
        this.ID = ID;
        this.inherentDesirability = inherentDesirability;
    }

    /**
     * Sets the connections from an array of references to connected systems
     * @param connectedSolarSystems an array containing references to all connected systems
     */
    public void setConnectedSystems(SolarSystem[] connectedSolarSystems){
        for(int i = 0; i < connectedSolarSystems.length; i++){
            this.connectedSolarSystems.add(connectedSolarSystems[i]);
        }
    }

    /**
     * Adds only one connected system to the connected systems field
     * @param connectedSolarSystem the connected system to add to the field
     */
    public void addConnectedSystem(SolarSystem connectedSolarSystem){
        //Adds the new system to this one's list of added systems
        this.connectedSolarSystems.add(connectedSolarSystem);
    }

    /**
     * Changes the inherentDesirability field's value
     * @param inherentDesirability the new value of the system's inherent desirability
     */
    public void setInherentDesirability(double inherentDesirability){
        this.inherentDesirability = inherentDesirability;
    }

    /**
     * Changes the inherentDesirability field's value, potentially temporarily.
     * @param inherentDesirability the new value for the system's inherent desirability
     * @param temporary whether the change is temporary
     */
    public void setInherentDesirability(double inherentDesirability, boolean temporary){
        if(temporary){
            inherentDesirabilityRESET = inherentDesirability;
            modified = true;
        }
        this.setInherentDesirability(inherentDesirability);
    }

    /**
     * Resets the system to its initial inherentDesirability
     */
    public void reset(){
        if(modified == true){
            visited = false;
            modified = false;
        }
    }

    /**
     * Changes the relationalDesirability based on the modification
     * @param modification the amount to change the relationalDesirability
     */
    public void modifyRelationalDesirability(double modification){
        this.relationalDesirability += modification;
    }

    /**
     * Gets the system Id
     * @return system ID
     */
    public int getID(){
        return ID;
    }

    /**
     * Gets the inherent desirability (the value of the system)
     * @return inherentDesirability
     */
    public double getInherentDesirability(){
        return calculateValue();
    }

    /**
     * Retrieves the relational desirability (the proximity to other valuable system)
     * @return the relational desirability
     */
    public double getRelationalDesirability(){
        return relationalDesirability;
    }

    /**
     * Retrieves a set containing the connected solar systems
     * @return set containing the solar systems connected to the current system
     */
    public Set<SolarSystem> getConnectedSolarSystems(){
        return connectedSolarSystems;
    }

    /**
     * Gets all systems within a particular range of the current system
     * @param range the number of jumps out to go before cutting off the growth of the set
     * @return a set containing all solar systems within range
     */
    public Set<SolarSystem> getConnectedSolarSystems(int range){
        Set<SolarSystem> systemsSet = new HashSet<SolarSystem>(connectedSolarSystems.size());
        for(SolarSystem solarSystem : connectedSolarSystems){
            systemsSet.add(solarSystem);
            if(range > 1) {
                systemsSet.addAll(solarSystem.getConnectedSolarSystems(range - 1));
            }
        }
        return systemsSet;
    }

    /**
     * Applies relational bonuses from systems within a given range to the current system
     * @param maxRange the range in which to consider effects
     */
    public void applyRadiatedEffects(int maxRange){
        relationalDesirability = 0;
        Set<SolarSystem> connectedSolarSystems;
        for(int i = 1; i < maxRange; i++){
            connectedSolarSystems = this.getConnectedSolarSystems(i);
            for(SolarSystem solarSystem : connectedSolarSystems){
                this.modifyRelationalDesirability(radiationValue(i, solarSystem));
            }
        }
    }

    /**
     * Helper function that converts a range and solar system to a relational bonus
     * @param range the range of the system bonusing the current one
     * @param solarSystem the system bonusing the current one
     * @return the value of the bonus
     */
    private double radiationValue(int range, SolarSystem solarSystem){
        return solarSystem.calculateValue() / Math.pow(16, range);
    }

    /**
     * Getter function for the total desirability of the solar system
     * @return the sum of the inherent and relational desirabilities
     */
    public double getTotalDesirability(){
        return getInherentDesirability() + getRelationalDesirability() - calculatedHazardRating(userThreshold);
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        Object[] returnObjects = new Object[]{getID(),getKills(),getPods(),getJumps(),getInherentDesirability(), getRelationalDesirability(), calculatedHazardRating(5),getTotalDesirability(), visited};
        for(int i = 0; i < returnObjects.length; i++){
            buffer.append(returnObjects[i]);
            buffer.append(", ");
        }
        return buffer.toString();
    }

    public void setVisited(boolean visited, boolean temporary){
        this.visited = visited;
        if(temporary){
            modified = true;
        }
        else{
            modified = false;
        }
    }

    public void setKills(int kills){
        this.kills = kills;
    }

    public void setJumps(int jumps){
        this.jumps = jumps;
    }

    public void setPods(int pods){
        this.pods = pods;
    }

    public int getKills(){
        return kills;
    }

    public int getJumps(){
        return jumps;
    }

    public int getPods(){
        return pods;
    }

    public void setSecurity(double security){
        this.security = security;
    }

    public double getSecurity(){
        return security;
    }

    public double calculateValue(){
        if(visited || security > 0){
            return 0;
        }
        if(jumps == 0){
            return 10;
        }
        if(-10 * Math.log10(jumps) + 10 > 0){
            return -10 * Math.log10(jumps) + 10;
        }
        else{
            return 0;
        }
    }

    /**
     * Calculates the number of kills, adjusted with the proportion of poddings and the number of warpgates in the system
     * @return double in interval (0.0, inf)
     */
    private double calculateAdjustedKills(){
        return kills * calculateMultipliers() / calculateDivisors();
    }

    /**
     * Calculates the multipliers for the kills
     * 0-1% poddings leads to a multiplier of .1 (effectively a divisor)
     * 50% poddings leads to a multiplier of 10 with no increase afterwards
     * @return double in interval (.1, 10)
     */
    private double calculateMultipliers() {
        if(kills == 0){
            return 0;
        }
        if (pods / kills < .01) {
            return 0.1;
        }
        else if(pods/kills < .5 && pods/kills > .01){
            return 5.786*Math.log10(pods/kills) + .1;
        }
        else{
            return 10;
        }
    }

    private double calculateDivisors(){
        if(connectedSolarSystems.size() > 0) {
            return connectedSolarSystems.size();
        }
        else{
            return 1;
        }
    }

    /**
     * Calculates a hazard rating, in the interval (0.0, 10.0) that represents how hazardous a system is based on the adjusted kills
     * @param userThreshold the number of adjusted kills in the system that the user is willing to tolerate before giving the system a max hazard rating
     * @return double in interval (0.0, 10.0)
     */
    public double calculatedHazardRating(double userThreshold){
        if(calculateAdjustedKills() == 0){ return 0;}
        else {
            return (9.9 / (Math.log10(userThreshold) + 2)) * Math.log10(calculateAdjustedKills()) + (.1 + 9.9 * 2 / (Math.log10(userThreshold) + 2));
        }
    }
}
