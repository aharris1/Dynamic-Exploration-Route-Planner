/**
 * Created by Andrew Harris on 2/27/2016.
 * Last Update: 2/27/2016
 */

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CSVtoUniverse {

    public static Universe generateUniverse(){
        File solarSystemsMap = new File("./mapSolarSystems.csv");
        File connections = new File("./mapSolarSystemJumps.csv");
        try{
            //Initialises the universe object with a Set of the SolarSystem objects parsed from the csv file
            Universe universe = new Universe(parseSystems(solarSystemsMap));
            //Stores the parsed connections as an array of integer pairs.
            int[][] links = parseConnections(connections);
            //Applies the links
            universe.addConnection(links);

            //Returns the completed universe
            return universe;
        }
        catch(Exception e){
            System.out.print(e);
        }
        return null;
    }

    public static Set<SolarSystem> parseSystems(File csvFile) throws IOException{
        CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        Set<SolarSystem> solarSystems = new HashSet<SolarSystem>();
        for(int i = 1; i < records.size(); i++){
            SolarSystem solarSystem = new SolarSystem(Integer.parseInt(records.get(i).get(2)));
            solarSystem.setSecurity(Double.parseDouble(records.get(i).get(21)));
            solarSystems.add(solarSystem);
        }
        return solarSystems;
    }

    public static int[][] parseConnections(File csvFile) throws IOException{
        CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        int[][] returnArray = new int[records.size() - 1][2];
        for(int i = 1; i < records.size(); i++){
            returnArray[i-1][0] = Integer.parseInt(records.get(i).get(2));
            returnArray[i-1][1] = Integer.parseInt(records.get(i).get(3));
        }
        return returnArray;
    }
}
