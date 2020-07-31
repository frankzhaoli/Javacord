import net.dv8tion.jda.api.AccountType;
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
        Main main=new Main();

        //get api keys from txt, store in map
        main.getBotToken();

        //initialize bot
        main.buildBot(keyMap.get("discord"));
    }

    //method to read in keys from external file
    private void getBotToken()
    {
        try
        {
            //read keys from file
            File file=new File("C:\\Users\\Frank\\IdeaProjects\\Javacord\\src\\main\\java\\keys.txt");
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
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(token);
            builder.addEventListeners(new BotListener(keyMap));
            builder.setActivity(Activity.listening("11:11"));
            builder.build();
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