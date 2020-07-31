import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BotListener extends ListenerAdapter
{
    private static HashMap<String, String> keyMap=new HashMap<>();

    public BotListener(HashMap <String, String> keyMap)
    {
        BotListener.keyMap=keyMap;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //get raw input command
        String command = event.getMessage().getContentRaw();

        //ignore all bots to avoid infinite loops
        if (event.getAuthor().isBot())
        {
            return;
        }

        //ensure commands begin with ".", ignore otherwise
        //ensure "..." doesn't trigger warnings
        if (command.startsWith(".") && Character.isLetter(command.charAt(1)))
        {
            //print commands
            System.out.println("We received a command from " + event.getAuthor().getName() + ": " + command);

            //force into lowercase
            command=command.toLowerCase();

            //show all available commands
            if (command.equals(".commands"))
            {
                //list commands
                event.getChannel().sendMessage("Available commands: " +
                        "\n .ping " +
                        "\n .weather " +
                        "\n .info" +
                        "\n .apod" +
                        "\n .kpod /group name/").queue();
            }

            //bot info
            else if (command.equals(".info"))
            {
                event.getChannel().sendMessage("Hello, I am a Java bot. Please use " +
                        ".commands to see my available commands.").queue();
            }

            //ping pong
            else if (command.equals(".ping"))
            {
                event.getChannel().sendMessage("Pong!").queue();
            }

            //show weather (comment with better input/output)
            else if (command.contains(".weather"))
            {
                System.out.println("."+command+".");

                if(command.length()>8)
                {
                    //not a great way
                    //but in this case should contain a city


                }

                String []split=command.split(" ");
                //String city=split[1];

                System.out.println(split[0]);
                //System.out.println(split[1]);
                System.out.println(split.length+"length????");


                if(split.length<2)
                {

                }


                /*
                if(split.length==1)
                {
                    event.getChannel().sendMessage("Please enter a city.").queue();
                }
                else
                {
                    Weather weather = new Weather(keyMap.get("openweather"), city);

                    try
                    {
                        weather.getWeather();
                        String summary = weather.toString();
                        event.getChannel().sendMessage(summary).queue();
                    }
                    catch (Exception e)
                    {
                        event.getChannel().sendMessage("Error, could not get the weather. Please try again later.").queue();
                    }
                }
                 */
            }

            //show nasa picture of the day
            else if (command.equals(".apod"))
            {
                APOD apod=new APOD(keyMap.get("apod"));

                try
                {
                    apod.getAPOD();
                    event.getChannel().sendMessage("Here is today's APOD from NASA:").queue();
                    event.getChannel().sendMessage(apod.getTitle()+", "+apod.getDate()).queue();
                    event.getChannel().sendMessage(apod.getExplanation()).queue();
                    event.getChannel().sendMessage(apod.getImageUrl()).queue();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    event.getChannel().sendMessage("Error, could not get APOD. Please try again later.").queue();
                }
            }

            //show a Kpop APOD
            else if(command.contains(".kpod"))
            {
                String []groups=new String[]{"twice", "aiyu", "kimtaeyeon", "blackpink", "kpopfap", "kpop", "blackpink"};
                List<String> groupsList= Arrays.asList(groups);

                String group=command.split(" ")[1].toLowerCase();

                if(groupsList.contains(group))
                {
                    KpopAPOD kpod = new KpopAPOD(group, keyMap.get("redditClientId"), keyMap.get("redditClientSecret"));

                    try
                    {
                        kpod.getKAPOD();
                        event.getChannel().sendMessage("Here is today's Kpop APOD (or gif) from r/"+group+".").queue();
                        event.getChannel().sendMessage(kpod.getURL()).queue();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        event.getChannel().sendMessage("Error, could not get Kpop APOD. Please try again later").queue();
                    }
                }
                else
                {
                    event.getChannel().sendMessage("Current /r not available. Please request from noona 2019.").queue();
                }
            }

            //
            else if (command.equals(".joke"))
            {

            }

            //
            else if (command.equals(".fun"))
            {

            }

            //
            else if (command.equals(".java"))
            {
                event.getChannel().sendMessage("https://docs.google.com/document" +
                        "/d/1kmy3le1ziTT5tvLQ_HmlDgmMyuSBfLm5OrsAXsGKq1s/edit?usp=sharing").queue();
            }

            //incorrect commands input by user
            else
            {
                event.getChannel().sendMessage("Error: not a command.").queue();
            }
        }
    }
}
