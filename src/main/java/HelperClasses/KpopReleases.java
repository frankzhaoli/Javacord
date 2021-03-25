package HelperClasses;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KpopReleases
{
    private final String month;
    private final String year;
    private final String clientID;
    private final String clientSecret;

    //necessary Reddit strings
    String userAgent="discord:version 1.0 (by u/xShiki69)";
    String accessTokenURL="https://www.reddit.com/api/v1/access_token"+"?grant_type=client_credentials";

    public KpopReleases(String month, String year, String clientID, String clientSecret)
    {
        this.month=month;
        this.year=year;
        this.clientID=clientID;
        this.clientSecret=clientSecret;
    }

    public void getJsonReleases() throws IOException
    {
        String groupURL="https://oauth.reddit.com/r/kpop/wiki/upcoming-releases/"+year+"/"+month+".json";
        String authString=clientID+":"+clientSecret;
        String encodedAuthString= Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

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
        BufferedReader redditPostIn=new BufferedReader(new InputStreamReader(redditPostConnection.getInputStream(), StandardCharsets.UTF_8));
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
        String contentmd=data.get("content_md").getAsString();

        contentmd=contentmd.replaceAll("\\r|\\n", "");

        String []contentmdArray=contentmd.split("\\|");
        List<String> contentmdList= Arrays.asList(contentmdArray);

        try
        {
            //PrintWriter writer=new PrintWriter("KpopReleases.txt", StandardCharsets.UTF_8);
            //PrintWriter writer=new PrintWriter(new File("C:\\Users\\Frank\\IdeaProjects\\Javacord\\kpopreleases\\KpopReleases "+month+" "+year+".html"), StandardCharsets.UTF_8);
            PrintWriter writer=new PrintWriter(new File("KpopReleases "+month+" "+year+".html"), StandardCharsets.UTF_8);
            writer.println("--KPOP RELEASES IN "+month.toUpperCase()+" "+year+"--");
            writer.println(contentmdList.get(0)+"\n");
            writer.println("Order:\n<br>Artist\n<br>Album Title\n<br>Album Type\n<br>Title Track\n<br>Streaming Link\n<br><br>");

            for (int i=0; i<contentmdList.size(); i++)
            {
                String current=contentmdList.get(i);
                //System.out.println(current);

                if (current.contains("spotify.com"))
                {
                    String artist=contentmdList.get(i-4);
                    String albumTitle=contentmdList.get(i-3);
                    String albumType=contentmdList.get(i-2);
                    String titleTrack=contentmdList.get(i-1);
                    String streamingLink=contentmdList.get(i);

                    //writer.println(artist+", "+albumTitle+", "+albumType+", "+titleTrack+", "+streamingLink);
                    writer.println("<b>"+artist+"</b><br>");
                    writer.println(albumTitle+"<br>");
                    writer.println(albumType+"<br>");
                    writer.println(titleTrack+"<br>");

                    //fix link
                    String spotifyLink=streamingLink.substring(streamingLink.indexOf("(")+1, streamingLink.indexOf(")"));
                    //System.out.println(spotifyLink);

                    writer.println("Spotify: <a href=\""+spotifyLink+"\" target=\"_blank\">"+"Link"+"</a>");
                    writer.println("<br><br>");
                    writer.println();
                }
            }

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
/*
    String fixLink(String link)
    {
        String spotifyLink="";
        System.out.println(spotifyLink);

        if(link.contains("spotify.com") && link.contains("apple.com"))
        {
            //String[] temp=link.split("/");
            //spotifyLink=temp[0];
        }

        //spotifyLink=spotifyLink.substring(spotifyLink.indexOf("(")+1, spotifyLink.indexOf(")"));
        //System.out.println(spotifyLink);
        return spotifyLink;
    }

 */
}
