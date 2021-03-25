package HelperClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class APOD
{
    private final String apodKey;
    private String title;
    private String date;
    private String explanation;
    private String imageUrl;


    public APOD(String apodKey)
    {
        this.apodKey=apodKey;
    }

    public void getAPOD() throws IOException
    {
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String currentDate=dateFormat.format(date);
        String requestURL="https://api.nasa.gov/planetary/apod?api_key="+apodKey+"&date="+currentDate;
        String json="";
        URL apodURL=new URL(requestURL);

        URLConnection connection=apodURL.openConnection();
        BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        while((inputLine=in.readLine())!=null)
        {
            json=inputLine;
        }
        in.close();

        parseJson(json);
    }

    public void parseJson(String jsonString)
    {
        JsonObject obj=new Gson().fromJson(jsonString, JsonObject.class);
        title=obj.get("title").getAsString();
        date=obj.get("date").getAsString();
        explanation=obj.get("explanation").getAsString();
        imageUrl=obj.get("url").getAsString();
    }

    public String getTitle()
    {
        return title;
    }

    public String getDate()
    {
        return date;
    }

    public String getExplanation()
    {
        return explanation;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }
}
