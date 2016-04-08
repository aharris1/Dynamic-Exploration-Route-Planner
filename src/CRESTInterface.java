import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Charsets;


import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew Harris on 2/28/2016.
 */
public class CRESTInterface {
    private static final HttpTransport HTTP = new NetHttpTransport();
    private static final String URL_SSO = "https://login.eveonline.com";
    private static final String URL_CREST = "https://crest-tq.eveonline.com/";

    /**
     * Gets the player's current location
     * @param authenticationCode the authentication token needed in the request
     * @return systemID of the player's current location
     */
    public static int currentLocation(String authenticationCode, int characterID){
        //Builds a URL for the
        StringBuffer buffer = new StringBuffer();
        buffer.append(URL_CREST);
        buffer.append("characters/");
        buffer.append(characterID);
        buffer.append("/location/");
        GenericUrl url = new GenericUrl(buffer.toString());

        //Generate the appropriate headers
        HttpHeaders headers = new HttpHeaders();
        headers.setUserAgent("DERP");
        headers.setAuthorization("Bearer " + authenticationCode);
        headers.set("Host", URL_SSO);

        //Prepares a regular expression to extract the data (hacky I know, but it'll work for now)
        Pattern p = Pattern.compile("\"id\": (\\d*)");
        Matcher m;

        //Sends the request and attempts to get a response
        HttpRequest request = null;
        HttpResponse response = null;
        InputStream JSONStream;
        try {
            request = HTTP.createRequestFactory().buildGetRequest(url).setHeaders(headers);
            response = request.execute();
            JSONStream = response.getContent();
            List<String> JSONLines = org.apache.commons.io.IOUtils.readLines(JSONStream, Charsets.UTF_8);
            String JSONLine = JSONLines.get(0);
            m = p.matcher(JSONLine);
            m.find();
            return Integer.parseInt(m.group(1));
        }
        catch(Exception e){
            System.out.println(e);
            return -1;

        }
    }

    /**
     * Adds the system with the systemID to the player's navigation
     * @param authenticationCode the authentication token needed in the request
     * @param systemID the systemID to add to the player's navigation
     * @param characterID the characterID of the explorer character
     */
    public static void addWaypoint(String authenticationCode, int characterID, int systemID){
        String POSTContent = "{\n" +
                "    \"clearOtherWaypoints\": false,\n" +
                "    \"solarSystem\": {\n" +
                "        \"href\": \"https://crest-tq.eveonline.com/solarsystems/" + systemID + "/\",\n" +
                "        \"id\":" + systemID + "},\n" +
                "    \n" +
                "    \"first\": false\n" +
                "}";
        HttpContent content = ByteArrayContent.fromString("application/json", POSTContent);
        String urlStr = URL_CREST + "/characters/"+ characterID + "/navigation/waypoints/";
        GenericUrl url = new GenericUrl(urlStr);

        HttpRequest request = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setUserAgent("DERP");
        headers.setContentType("application/json");
        headers.setAuthorization("Bearer " + authenticationCode);
        headers.set("Host" , URL_CREST);

        HttpResponse response = null;

        try{
            request = HTTP.createRequestFactory().buildPostRequest(url, content);
            request.setHeaders(headers);
            response = request.execute();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public static int getCharacterID(String authenticationCode){
        StringBuffer buffer = new StringBuffer();
        buffer.append(URL_SSO);
        buffer.append("/oauth/verify/");
        GenericUrl url = new GenericUrl(buffer.toString());

        //Generate the appropriate headers
        HttpHeaders headers = new HttpHeaders();
        headers.setUserAgent("DERP");
        headers.setAuthorization("Bearer " + authenticationCode);
        headers.set("Host", URL_SSO);

        //Prepares a regular expression to extract the data (hacky I know, but it'll work for now)
        Pattern p = Pattern.compile("ID\":(\\d*)");
        Matcher m;

        //Sends the request and attempts to get a response
        HttpRequest request = null;
        HttpResponse response = null;
        InputStream JSONStream;
        try {
            request = HTTP.createRequestFactory().buildGetRequest(url).setHeaders(headers);
            response = request.execute();
            JSONStream = response.getContent();
            List<String> JSONLines = org.apache.commons.io.IOUtils.readLines(JSONStream, Charsets.UTF_8);
            String JSONLine = JSONLines.get(0);
            System.out.println(JSONLine);
            m = p.matcher(JSONLine);
            m.find();
            return Integer.parseInt(m.group(1));
        }
        catch(Exception e){
            System.out.println(e);
            return -1;
        }
    }
}
