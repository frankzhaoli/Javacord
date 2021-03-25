package HelperClasses;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class KpopAPOD
{
    private String imageUrl;

    private final String group;
    private final String clientID;
    private final String clientSecret;

    //necessary Reddit strings
    String userAgent="discord:version 1.0 (by u/xShiki69)";
    String accessTokenURL="https://www.reddit.com/api/v1/access_token"+"?grant_type=client_credentials";

    //initialize group, clientID, clientSecret
    public KpopAPOD(String group, String clientID, String clientSecret)
    {
        this.group=group;
        this.clientID=clientID;
        this.clientSecret=clientSecret;
    }

    //do the web calls to get json
    public void getKAPOD() throws IOException
    {
        String groupURL="https://oauth.reddit.com/r/"+group+"/new.json";
        String authString=clientID+":"+clientSecret;
        String encodedAuthString=Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

        //first web call for access token
        URL authURL=new URL(accessTokenURL);
        HttpURLConnection authConnection=(HttpURLConnection) authURL.openConnection();
        authConnection.setRequestMethod("POST");
        authConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        authConnection.setRequestProperty("Authorization", "Basic "+encodedAuthString);
        authConnection.setRequestProperty("grant_type", "client_credentials");
        authConnection.setRequestProperty("User-Agent", userAgent);

        String accessToken;

        //get input and read into string
        BufferedReader tokenIn=new BufferedReader(new InputStreamReader(authConnection.getInputStream()));
        String inputLine;
        String accessTokenJsonString="";

        while((inputLine=tokenIn.readLine())!=null)
        {
            accessTokenJsonString=inputLine;
        }
        tokenIn.close();

        //extract access token
        JsonObject obj=new Gson().fromJson(accessTokenJsonString, JsonObject.class);
        accessToken=obj.get("access_token").getAsString();

        //second web call using access token to get reddit post json
        URL postURL=new URL(groupURL);
        HttpURLConnection redditPostConnection=(HttpURLConnection) postURL.openConnection();
        redditPostConnection.setRequestMethod("GET");
        redditPostConnection.setRequestProperty("Authorization", "Bearer "+accessToken);
        redditPostConnection.setRequestProperty("User-Agent", userAgent);
        redditPostConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        //get input and read into string
        BufferedReader redditPostIn=new BufferedReader(new InputStreamReader(redditPostConnection.getInputStream()));
        String inputLine2;
        String redditPostJsonString = "";

        while((inputLine2=redditPostIn.readLine())!=null)
        {
            redditPostJsonString=inputLine2;
        }
        redditPostIn.close();

        parseJson(redditPostJsonString);
    }

    public void parseJson(String jsonString)
    {
        JsonObject obj=new Gson().fromJson(jsonString, JsonObject.class);
        JsonObject data=obj.getAsJsonObject("data");
        JsonArray children=data.getAsJsonArray("children");

        JsonObject childData;

        List<String> urlList=new ArrayList<String>();

        for(int i=0; i<24; i++)
        {
            JsonObject child=(JsonObject) children.get(i);
            childData=child.getAsJsonObject("data");

            imageUrl=childData.get("url").getAsString();

            if(imageUrl.contains("streamable") || imageUrl.contains("gfycat") || imageUrl.contains("redd.it") || imageUrl.contains("imgur"))
            {
                urlList.add(imageUrl);
            }
        }

        int randNum= (int) (Math.random() * 24);
        imageUrl=urlList.get(randNum);
    }

    public String getURL()
    {
        return imageUrl;
    }
}
