import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sun.rmi.runtime.Log;

import java.lang.*;
import java.io.*;
import java.util.*;


import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws Exception{
        Main main=new Main();
        String token=main.getBotToken();
        main.buildBot(token);
    }

    private String getBotToken() throws FileNotFoundException {
        File file=new File("C:\\Users\\Frank\\IdeaProjects\\Javacord\\src\\main\\java\\DiscordToken.txt");
        Scanner scan=new Scanner(file);
        String token=scan.nextLine();
        return token;
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
