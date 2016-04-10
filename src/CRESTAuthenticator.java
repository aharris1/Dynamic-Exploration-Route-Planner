import com.google.api.client.auth.oauth2.*;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew Harris on 2/27/2016.
 * Last Modified: 2/27/2016
 * <p/>
 * Class to retrieve the authentication token necessary for the authenticated CREST calls
 * Heavily modified source from eve-crest OAuthHelper.java class
 * https://bitbucket.org/evanova/eve-crest/src/2f847da12f9bbb4c54d75960e2925180335d59fc/src/main/java/com/tlabs/eve/crest/net/OAuthHelper.java
 * <p/>
 * Permissions Required:
 * publicData
 * characterLocationRead
 * characterNavigationWrite
 */
public class CRESTAuthenticator {

    private static final String[] SCOPES = {"publicData", "characterLocationRead", "characterNavigationWrite"};
    //TODO: Allow user to generate their own ClientID and Client Secret and substitute them for those provided
    private static final String CLIENT_ID = "bc74a849fd32403983936b4b22519a00";
    private static final String CLIENT_SECRET = "WOEyJ6EZXznIW9L4wCMGPmEmgLkpJqJDmoUgJ74e";
    private static final String LOGIN_URL = "https://login.eveonline.com/";
    private static final HttpTransport HTTP = new NetHttpTransport();
    private static final JsonFactory JSON = new JacksonFactory();

    private AuthorizationCodeFlow flow;

    private final String userID;

    public CRESTAuthenticator(String userID) {
        this.userID = userID;
        try {

            this.flow = new AuthorizationCodeFlow.Builder(
                    BearerToken.authorizationHeaderAccessMethod(),
                    HTTP,
                    JSON,
                    new GenericUrl(LOGIN_URL + "/oauth/token"),
                    new ClientParametersAuthentication(CLIENT_ID, CLIENT_SECRET),
                    CLIENT_ID,
                    LOGIN_URL + "/oauth/authorize").
                    setDataStoreFactory(MemoryDataStoreFactory.getDefaultInstance()).
                    setScopes(Arrays.asList(SCOPES)).
                    build();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public String start(){
        final Credential credential = loadCredentials();
        if(null == credential){
            return this.flow.newAuthorizationUrl().setRedirectUri("https://github.com/aharris1/SemirandomWalk").build();
        }
        return null;
    }

    public String[] authorize(final String authCode){
        if(authCode.isEmpty()){
            final Credential credential = loadCredentials();
            if(null == credential){
                return null;
            }
            else{
                return new String[]{credential.getAccessToken(), credential.getRefreshToken()};
            }
        }
        try {
            final TokenResponse tokenResponse = this.flow.newTokenRequest(authCode).execute();
            final Credential credential = this.flow.createAndStoreCredential(tokenResponse, this.userID);
            if (null == credential) {
                return null;
            } else {
                return new String[]{credential.getAccessToken(), credential.getRefreshToken()};
            }
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    public String getCredentials(){
        Credential credential = loadCredentials();
        if(null == credential){
            return null;
        }
        else{
            return credential.getAccessToken();
        }
    }

    public Credential loadCredentials(){
        try{
            return this.flow.loadCredential(this.userID);
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    public String refresh(String refreshCode){
        GenericUrl url;

        try{
            url = new GenericUrl("https://login.eveonline.com/oauth/token");

            StringBuffer buffer = new StringBuffer();
            buffer.append("grant_type=refresh_token&refresh_token=");
            buffer.append(refreshCode);
            HttpRequest POST = HTTP.createRequestFactory().buildPostRequest(url, ByteArrayContent.fromString("application/x-www-form-urlencoded", buffer.toString()));
            HttpHeaders headers = new HttpHeaders();
            headers.setAuthorization("Basic " + getEncodedID());
            headers.setContentType("application/x-www-form-urlencoded");
            headers.set("Host", "login.eveonline.com");
            POST.setHeaders(headers);
            HttpResponse response = POST.execute();
            String rawString = org.apache.commons.io.IOUtils.toString(response.getContent(), Charset.defaultCharset());
            Pattern pattern = Pattern.compile("access_token\":\"([^\"]*)\"");
            Matcher matcher = pattern.matcher(rawString);
            matcher.find();
            return matcher.group(1);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static String getEncodedID(){
        return Base64.encodeBase64String((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
    }

}

