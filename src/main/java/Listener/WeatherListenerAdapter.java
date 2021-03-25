package Listener;

import HelperClasses.Weather;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WeatherListenerAdapter extends ListenerAdapter
{
    private final String key;

    public WeatherListenerAdapter(String key)
    {
        this.key=key;
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
            System.out.println("We received a command from " + event.getAuthor().getName() + ": " + command + ".");

            //force into lowercase
            command=command.toLowerCase();

            //show weather (comment with better input/output)
            if (command.contains(".weather"))
            {
                //check for city in command
                if(command.equals(".weather"))
                {
                    event.getChannel().sendMessage("Error, missing the city. Please try (.weather some-city) without the parentheses.").queue();
                }
                else
                {
                    String []split=command.split(" ");
                    String city=split[1];

                    //leaving here as a note for future reference
                    //if command is .weather without the city
                    //causes outofboundsexception (or some other exception) but doesn't get caught (???)
                    //System.out.println(split[1]);

                    Weather weather = new Weather(key, city);

                    try
                    {
                        weather.getWeather();
                        String summary = weather.toString();
                        event.getChannel().sendMessage(summary + "test").queue();
                    }
                    catch (Exception e)
                    {
                        event.getChannel().sendMessage("Error, could not get the weather. Please try again later.").queue();
                    }
                }
            }
        }
    }
}
