import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by O675 on 2/29/2016.
 */
public class XMLAPIParser {
    private static final GenericUrl JUMPS_URL = new GenericUrl("https://api.eveonline.com/map/jumps.xml.aspx");
    private static final GenericUrl KILLS_URL = new GenericUrl("https://api.eveonline.com/map/kills.xml.aspx");
    private static final HttpTransport HTTP = new NetHttpTransport();
    private static Matcher m;
    private static String line;
    /**
     * Extracts the kills from the XML API and returns the kills in the last hour in the form
     * {SystemID, Kills_lastHour, Pods_lastHour}
     * @return {SystemID, Kills_lastHour, Pods_lastHour}
     */
    public static int[][] getKills(){

        boolean pastResult = false;
        boolean pastHeaders = false;
        boolean beforeEnd = true;
        List<int[]> killsList = new ArrayList<int[]>();
        try {
            HttpRequest request = HTTP.createRequestFactory().buildGetRequest(KILLS_URL);
            HttpResponse response = request.execute();
            InputStream xmlResponse = response.getContent();
            List<String> lines = IOUtils.readLines(xmlResponse, Charset.defaultCharset());
            Pattern p = Pattern.compile("solarSystemID=\"(\\d*).*shipKills=\"(\\d*).*podKills=\"(\\d*)");
            Matcher m;
            String line;
            for(int i = 0; i < lines.size(); i++){
                line = lines.get(i);
                if(line.contains("</rowset>")){
                    beforeEnd = false;
                }
                if(pastResult && pastHeaders && beforeEnd){
                    m=p.matcher(line);
                    m.find();
                    killsList.add(new int[]{Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))});
//                    System.out.println(m.group(1) + ", " + m.group(2) + ", " + m.group(3));
                }
                if(line.contains("<result>")){
                    pastResult = true;
                }
                if(line.contains("<rowset name")){
                    pastHeaders = true;
                }
            }
            int[][] returnArray = new int[killsList.size()][3];
            killsList.toArray(returnArray);
            return returnArray;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return new int[][]{{0,0}};
    }

    public static int[][] getJumps(){
        boolean pastResult = false;
        boolean pastHeaders = false;
        boolean beforeEnd = true;
        List<int[]> jumpsList = new ArrayList<int[]>();
        try {
            List<String> lines = getXMLLines();
            Pattern p = Pattern.compile("solarSystemID=\"(\\d*).*shipJumps=\"(\\d*)");

            for(int i = 0; i < lines.size(); i++){
                line = lines.get(i);
                if(line.contains("</rowset>")){
                    beforeEnd = false;
                }
                if(pastResult && pastHeaders && beforeEnd){
                    m=p.matcher(line);
                    m.find();
                    jumpsList.add(new int[]{Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2))});
                }
                if(line.contains("<result>")){
                    pastResult = true;
                }
                if(line.contains("<rowset name")){
                    pastHeaders = true;
                }
            }
            int[][] returnArray = new int[jumpsList.size()][3];
            jumpsList.toArray(returnArray);
            //<row solarSystemID="30004995" shipJumps="23"/>
            return returnArray;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return new int[][]{{0,0}};
    }

    private static List<String> getXMLLines() throws IOException{
        HttpRequest request = HTTP.createRequestFactory().buildGetRequest(JUMPS_URL);
        HttpResponse response = request.execute();
        InputStream xmlResponse = response.getContent();
        List<String> lines = IOUtils.readLines(xmlResponse, Charset.defaultCharset());
        return lines;
    }

}
