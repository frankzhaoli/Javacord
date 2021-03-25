package HelperClasses;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

//class that returns weather information
public class Weather {
    private final String apiKey;
    private String city;
    private String summary;

    //initialize weather object with city
    public Weather(String apiKey, String city)
    {
        this.apiKey=apiKey;
        this.city=city;
    }

    public void getWeather() throws IOException
    {
        String requestURL="https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=imperial&appid="+apiKey;
        String openWeatherJson="";
        URL openWeatherURL=new URL(requestURL);

        URLConnection connection=openWeatherURL.openConnection();
        BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        while((inputLine=in.readLine()) != null)
        {
            openWeatherJson=inputLine;
        }
        in.close();

        parseJson(openWeatherJson);
    }

    public void parseJson(String jsonString)
    {
        JsonObject obj=new Gson().fromJson(jsonString, JsonObject.class);
        JsonArray weatherArray=obj.get("weather").getAsJsonArray();
        JsonObject weatherObj=weatherArray.get(0).getAsJsonObject();

        String description=weatherObj.get("description").getAsString();

        JsonObject mainTempObj=obj.getAsJsonObject("main");
        String temp=mainTempObj.get("temp").getAsString();
        String feelsLikeTemp=mainTempObj.get("feels_like").getAsString();

        summary="The weather in "+city+" is "+description+". The temperature is "
                +temp+", and it feels like "+feelsLikeTemp+".";
    }

    public String getCity()
    {
        return this.city;
    }

    public void setCity()
    {
        this.city=city;
    }

    @Override
    public String toString()
    {
        return summary;
    }
}

