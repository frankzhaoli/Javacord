import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.util.*;


//class that returns weather information
public class Weather {
    private String apiKey;
    private String city;

    public Weather(String apiKey, String city)
    {
        this.apiKey=apiKey;
        this.city=city;
    }

    public void getWeather() throws IOException {
        String requestURL="https://api.darksky.net/forecast/e55bcbfa7652e292fadfe151611c1e82/37.8267,-122.4233";
        URL darksky=new URL(requestURL);

        URLConnection connection=darksky.openConnection();
        BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        while((inputLine=in.readLine()) != null)
            System.out.println(inputLine);
        in.close();

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
        return null;
    }

}

