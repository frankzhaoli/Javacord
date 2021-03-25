package Listener;

import HelperClasses.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BotListener extends ListenerAdapter
{
    private final String clientID;
    private final String clientSecret;

    public BotListener(String clientID, String clientSecret)
    {
        this.clientID=clientID;
        this.clientSecret=clientSecret;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();

        //get raw input command
        String command = event.getMessage().getContentRaw();

        //ignore all bots to avoid infinite loops
        if (event.getAuthor().isBot())
        {
            return;
        }

        //System.out.println(event.getChannel().toString());
        //testing channel specific msgs
        //TC:general(216407322765950976) (for general channel id)
        if(event.getChannel().toString().equals("TC:pokimon(546125231325052929)"))
        {
            //event.getChannel().sendMessage("testing channel specific msgs.").queue();
        }

        //ensure commands begin with ".", ignore otherwise
        //ensure "..." doesn't trigger warnings
        if (command.startsWith(".") && Character.isLetter(command.charAt(1)))
        {
            //print commands
            System.out.println("We received a command from " + event.getAuthor().getName() + ": " + command + ".");

            //force into lowercase
            command=command.toLowerCase();

            //show all available commands
            if (command.equals(".help"))
            {
                //list commands
                event.getChannel().sendMessage("Available commands: " +
                        "\n .info" +
                        "\n .ping " +
                        "\n .weather some-city" +
                        "\n .apod" +
                        "\n .kpod subreddit-name (Options: twice, aiyu, kimtaeyeon, blackpink, kpopfap, kpop)" +
                        "\n .kpopreleases some-month some-year ex. \"january 2021\"" +
                        "\n .reminders" +
                        "\n .java").queue();
            }

            //bot info
            else if (command.equals(".info"))
            {
                event.getChannel().sendMessage("Hello, I am a Java bot. Use .commands to see more.").queue();
            }

            //ping pong
            else if (command.equals(".ping"))
            {
                event.getChannel().sendMessage("Pong!").queue();
            }

//            //show nasa picture of the day
//            else if (command.equals(".apod"))
//            {
//                APOD apod=new APOD(provider.getApodKey());
//
//                try
//                {
//                    apod.getAPOD();
//                    event.getChannel().sendMessage("Here is today's APOD from NASA:").queue();
//                    event.getChannel().sendMessage(apod.getTitle()+", "+apod.getDate()).queue();
//                    event.getChannel().sendMessage(apod.getExplanation()).queue();
//                    event.getChannel().sendMessage(apod.getImageUrl()).queue();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                    event.getChannel().sendMessage("Error, could not get APOD. Please try again later.").queue();
//                }
//            }

            //show a Kpop APOD
            else if(command.contains(".kpod"))
            {
                if(command.equals(".kpod"))
                {
                    event.getChannel().sendMessage("Error, missing the subreddit. Please try (.kpod subreddit-name) without the parentheses. For possible options, use .help.").queue();
                }
                else
                {
                    String []groups=new String[]{"twice", "aiyu", "kimtaeyeon", "blackpink", "kpopfap", "kpop"};
                    List<String> groupsList= Arrays.asList(groups);

                    String group=command.split(" ")[1];

                    if(groupsList.contains(group))
                    {
                        KpopAPOD kpod = new KpopAPOD(group, clientID, clientSecret);
                        System.out.println(clientID + " " + clientSecret);
                        try
                        {
                            kpod.getKAPOD();
                            event.getChannel().sendMessage("Here is today's Kpop APOD (or gif) from r/"+group+".").queue();
                            event.getChannel().sendMessage(kpod.getURL()).queue();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                            event.getChannel().sendMessage("Error, could not get Kpop APOD. Please try again later.").queue();
                        }
                    }
                    else
                    {
                        event.getChannel().sendMessage("Current /r not available. Please request from noona 2019.").queue();
                    }
                }
            }

            //create a txt file of kpop releases from reddit
            else if(command.contains(".kpopreleases"))
            {
                //check for month in command
                if(command.equals(".kpopreleases"))
                {
                    event.getChannel().sendMessage("Error, missing the month and/or year. Please try \".kpopreleases some-month some-year\".").queue();
                }
                else
                {
                    event.getChannel().sendMessage("Please wait...").queue();
                    String month=command.split(" ")[1];
                    //array issue here again, need some way to check for year, seems difficult bc of 3 different inputs
                    //could get last digit of input, if its a number-proceed, otherwise there's no year xd
                    String year=command.split(" ")[2];

                    //CHECK FOR CORRECT SPELLING OF MONTH HERE
                    KpopReleases releases=new KpopReleases(month, year, clientID, clientSecret);

                    try
                    {
                        releases.getJsonReleases();
                        //sends the file to discord
                        //event.getChannel().sendMessage("Here's the releases for "+month+" "+year+" in a text file.").addFile(new File("C:\\Users\\Frank\\IdeaProjects\\Javacord\\kpopreleases\\KpopReleases "+month+" "+year+".html")).queue();
                        event.getChannel().sendMessage("Here's the releases for "+month+" "+year+".").addFile(new File("KpopReleases "+month+" "+year+".html")).queue();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        event.getChannel().sendMessage("Error, could not get Kpop releases. Please try again later/ensure your parameters are correct.").queue();
                    }
                }
            }

            else if(command.equals(".solo"))
            {
                int daysToSoloLeveling=(Calendar.WEDNESDAY-calendar.get(Calendar.DAY_OF_WEEK)+7)%7;

                if(daysToSoloLeveling==0)
                {
                    event.getChannel().sendMessage("A new chapter of Solo Leveling has been released.").queue();
                }
                else
                {
                    event.getChannel().sendMessage("A new chapter of Solo Leveling will be released in "+daysToSoloLeveling+" days.").queue();
                }
                event.getChannel().sendMessage("https://w2.sololeveling.net/").queue();
            }

            //testing sending links to discord
            else if (command.equals(".java"))
            {
                event.getChannel().sendMessage("Here's my source code:").queue();
                event.getChannel().sendMessage(": )").queue();
                event.getChannel().sendMessage("https://github.com/frankzhaoli/Javacord/tree/master").queue();
            }
        }
    }
}
