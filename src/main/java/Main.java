import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.*;
import java.io.*;
import java.util.*;
import javax.security.auth.login.LoginException;

//main method
public class Main extends ListenerAdapter {
    public static void main(String[] args) throws Exception{
        Main main=new Main();
        //get api keys from txt, stored in array
        String[] token=main.getBotToken();
        String discordKey=token[0];
        String darkSkyKey=token[1];
        //initialize bot
        //main.buildBot(discordKey);
        Weather weather=new Weather("asdf", "houston");
        weather.getWeather();

    }

    //method to read in bot token
    private String[] getBotToken() throws FileNotFoundException {
        //array to hold keys
        String[] keys=new String[5];
        //read keys from file
        File file=new File("C:\\Users\\Frank\\IdeaProjects\\Javacord\\src\\main\\java\\keys.txt");
        Scanner scan=new Scanner(file);

        //read and parse until eof
        int i=0;
        while(scan.hasNextLine())
        {
            String token=scan.nextLine();
            token=token.split(":")[1];
            keys[i]=token;
            i++;
        }

        return keys;
    }

    private void buildBot(String token) throws LoginException
    {
        JDABuilder builder=new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //ignore all bots
        if(event.getAuthor().isBot() && event.getAuthor().getName() != "noona 2019")
        {
            return;
        }

        System.out.println("We received a msg from "+event.getAuthor().getName()+
                ": "+event.getMessage().getContentDisplay());

        //get raw input
        if(event.getMessage().getContentRaw().equals("!ping"))
        {
            //always call queue
            //otherwise msg will never be sent
            event.getChannel().sendMessage("Pong!").queue();
        }
    }
}
