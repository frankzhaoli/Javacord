package Main;

import Listener.BotListener;
import Listener.WeatherListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.lang.*;
import java.io.*;
import java.util.*;
import javax.security.auth.login.LoginException;

public class Main
{
    private static final HashMap <String, String> keyMap= new HashMap<>();

    public static void main(String[] args)
    {
        System.out.println("Starting Javacord: ");
        Main main=new Main();

        //get api keys from txt, store in map
        main.getBotToken();

        //initialize bot
        main.buildBot(keyMap.get("discord"));
        System.out.println("Starting complete. ");
    }

    //method to read in keys from external file
    private void getBotToken()
    {
        try
        {
            //read keys from file
            File file=new File("src/main/resources/keys.txt");
            //File file=new File("keys.txt");
            Scanner scan=new Scanner(file);

            //read and parse until eof
            while(scan.hasNextLine())
            {
                String line=scan.nextLine();
                //split at ":"
                String[] split =line.split(":");
                //to clarify: key/value pairs in a map
                //current keys: discord, openweather, apod, redditClientID, redditClientSecret
                String key=split[0];
                String value=split[1];
                keyMap.put(key, value);
            }

            //empty keyMap
            //keyMap.clear();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //set token build bot
    private void buildBot(String token)
    {
        try
        {
            JDA builder=JDABuilder.createDefault(token).setActivity(Activity.listening("11:11")).build();
            builder.addEventListener(new BotListener(keyMap.get("redditClientId"), keyMap.get("redditClientSecret")));
            builder.addEventListener(new WeatherListenerAdapter(keyMap.get("openweather")));
            //builder.addEventListener(new APODListenerAdapter(keyMap.get("apod")));
            //builder.addEventListener(new KPODListenerAdapter(keyMap.get("redditClientId"), keyMap.get("redditClientSecret"));
        }
        catch (LoginException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

        //snippet for debugging
        /*
        ConsoleHandler handler=new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger log= LogManager.getLogManager().getLogger("");
        log.addHandler(handler);
        log.setLevel(Level.ALL);
         */