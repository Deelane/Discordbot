package events;

import java.util.List;

public class MessageRouter
{
    public static String RouteMessage(List<String> Parsedmsg)
    {
        String result = "";
        MessageEvent event = null;
        switch(Parsedmsg.get(0)) //Parsedmsg[0] is the first word that the user types, this switch routes it to the correct method depending on what !command the user typed.
        {
            case "!roll":
                event = new RollEvent();
                break;
            case "!strawpoll":
                event = new StrawPollEvent();
                break;
            case "!lolbuild":
                event = new LolBuildEvent();
            default:
                result = "Not a valid command.";
        }
        if (event != null)
        {
            event.setParameters(Parsedmsg);
            result = event.doAction(Parsedmsg);
        }
        return result;
    }
}
